package com.example.aplicacioncon4botones

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
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
import com.example.aplicacioncon4botones.databinding.ActivityTlfBinding

/**
 * Clase que sirve para dar funciones a la pantalla principal del boton llamada, en donde se aloja:
 * 1.Boton para llamar
 * 2.Boton para cambiar de telefono
 * 3.boton para volver al menu principal
 *
 * @author Daniel
 * @Version 1.0
 */

class CallScreen : AppCompatActivity() {
    private lateinit var configBind : ActivityTlfBinding

    private lateinit var launcherPermission : ActivityResultLauncher<String>

    private var permissionForCall = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configBind = ActivityTlfBinding.inflate(layoutInflater)
        setContentView(configBind.root)
        enableEdgeToEdge()

        start()
    }

     private fun start() {
         //Cada que entremos y obtengamos los valores del archivo compartido, obtendremos el numero que se supone está escrito
         //En caso de que no exista, el texto estará vacio (Algo que en condiciones normales no debe de pasar)
         var nameShared : SharedPreferences = getSharedPreferences(getString(R.string.fitch_preferences), Context.MODE_PRIVATE)
         var mod = nameShared.getString(getString(R.string.placeholder), " ")
         configBind.telefonoMostrador.setText(mod)
         Log.d("/////Num////", mod!!)
         launcherP()



         configBind.telephoneActive.setOnClickListener {

             if (!permissionForCall) {
                 if (applicationInfo.targetSdkVersion < 23 ||
                     ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                 {permissionForCall = true}
                 else {
                     launcherPermission.launch(Manifest.permission.CALL_PHONE)
                      }
                 }
             if (permissionForCall) {
                 call()
             }
         }
         configBind.changePh.setOnClickListener{
             val nameSh = getString(R.string.fitch_preferences)
             val numb = getString(R.string.placeholder)

             getSharedPreferences(nameSh, Context.MODE_PRIVATE).edit().remove(numb).apply()
             val intencion = Intent(this, ConfigForCall::class.java).apply {
                 putExtra("back", true)
                 addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP)
             }
             startActivity(intencion)

         }
         configBind.backMain.setOnClickListener {
             val intencion = Intent(this, Activity::class.java)
             startActivity(intencion)
         }


    }

    private fun launcherP(){
        launcherPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted ->
                if (isGranted) {
                    permissionForCall = true
                } else {
                    Toast.makeText(this, "Necesitas permisos", Toast.LENGTH_LONG).show()
                    configCall()
                }
        }
    }

    private fun configCall(){
        var intencion = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:" + packageName)
        }
        startActivity(intencion)
    }

    private fun call(){
        var intencion = Intent(Intent.ACTION_CALL).apply {
            Log.d("//--//","${configBind.telefonoMostrador.text}")
            data = Uri.parse("tel: ${configBind.telefonoMostrador.text}")
        }
        startActivity(intencion)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // Actualiza el Intent con los nuevos extras
    }

}