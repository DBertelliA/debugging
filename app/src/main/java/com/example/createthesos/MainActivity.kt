package com.example.createthesos

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.createthesos.databinding.ActivityPrincipalBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainContainer : ActivityPrincipalBinding

    private var phoneSOS : String? = null

    private lateinit var requestPermissionLauncher : ActivityResultLauncher<String> //Esto es para lanzar los permisos

    private var alreadyPermitted = false

    private var valorParaCambiar1 = 0

    private var valorParaCambiar2 = 99


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("---Salida Create---", "Se ha ejecutado el onCreate del mainActivity")

        mainContainer = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(mainContainer.root)

        enableEdgeToEdge()
        init()
        initEventCall()
    }

    override fun onResume() {
        super.onResume()
        Log.d("---Salida Resume---", "Se ha ejecutado el onResume")


        alreadyPermitted = isPermissionCall()
        val stringPhone = getString(R.string.phone_string) //Este es para que obtenga el id
        phoneSOS = intent.getStringExtra(stringPhone)
        mainContainer.numberHolder.setText(phoneSOS)
    }

    private fun init(){
        mainContainer.button6.setOnClickListener {
            onPause()
        }
        mainContainer.button4.setOnClickListener {
            onRestart()
        }
        mainContainer.button5.setOnClickListener {
            onStop()
        }
        mainContainer.button3.setOnClickListener {
            onDestroy()
        }
        registerLauncher()
        if (!isPermissionCall()){
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }

        mainContainer.changePhone.setOnClickListener {
//            var nameShareFich = getString(R.string.name_preference_share_fich)
//            var namePhoneSH = getString(R.string.phone_string)
//
//            var mod = getSharedPreferences(nameShareFich, Context.MODE_PRIVATE)
//            mod.edit().remove(namePhoneSH).apply()
            //esto de arriba no deberia estar aqui
            val intent = Intent(this, ConfigAct::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtra("back", true) //Volvemos al activity anterior con el valor este a true
            }
            startActivity(intent)
        }

    }


    //lo de los launcher
    //Y los permisos es una locura
    private fun registerLauncher(){
        //el launch lanza lo que has construido aqui!!!
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
                if(isGranted){
                    alreadyPermitted = true
                }
                else{
                    Toast.makeText(this, "Necesitas habilitar los permisos", Toast.LENGTH_LONG).show()
                    goToConfApp()
                }
        }

    }

    private fun isPermissionToUser() = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

    private fun isPermissionCall():Boolean{
        //Debido a un error muy gordo de los desarrolladores de android, si la API es inferior al 23, los permisos se aceptan automaticamente
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true
        }else{
            return isPermissionToUser()
        }
    }

    //Contructor para accionar la llamada o oreguntar por permisos
    private fun initEventCall(){
        mainContainer.buttonCall.setOnClickListener {
            alreadyPermitted = isPermissionCall()
            if (alreadyPermitted){
                call()
            }else{
                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

    //Los intents se usan para abrir otras cosas... tenerlo muy en cuenta

    private fun call(){
        //Dependiendo de que intent le tengamos que poner le tendremos que poner un Uri diferente
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:"+phoneSOS!!)
        }
        startActivity(intent)
        var suma = valorParaCambiar2 + valorParaCambiar1
    }

    private fun goToConfApp(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // Actualiza el Intent con los nuevos extras
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "Pausado?", Toast.LENGTH_SHORT).show()
        Log.d("---Salida Pause---","Acabo de ejecutar el OnPause")
        valorParaCambiar1++
    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this, "He vuelto a la aplicacion", Toast.LENGTH_SHORT).show()
        Log.d("---Salida Restart---","Acabo de ejecutar el OnRestart")
        valorParaCambiar1++
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "Me he salido y no soy reestart", Toast.LENGTH_SHORT).show()
        Log.d("---Salida Stop---","Acabo de ejecutar el OnStop")
        valorParaCambiar2--
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Bowling", Toast.LENGTH_SHORT).show()
        Log.d("---Salida destroy---","Acabo de ejecutar el OnDestroy")
        valorParaCambiar2--
    }

}