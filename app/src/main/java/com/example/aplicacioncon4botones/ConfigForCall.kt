package com.example.aplicacioncon4botones

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacioncon4botones.databinding.ChangePhoneBinding
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

class ConfigForCall : AppCompatActivity() {
    private lateinit var confBing : ChangePhoneBinding

    private lateinit var shareFich : SharedPreferences

    private var phoneSh : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        confBing = ChangePhoneBinding.inflate(layoutInflater)
        setContentView(confBing.root)

        enableEdgeToEdge()

        inicioDeLosShared()
        start()
    }

    override fun onResume() {
        super.onResume()
        var reten = intent.getBooleanExtra("back", false)

        if(reten){
            confBing.fieldForN.setText("")
            Toast.makeText(this, "Introduce el numero", Toast.LENGTH_LONG).show()

            intent.removeExtra("back")
        }

    }


    private fun start() {

        //Que resete el numero de la carpeta cada que se entre a aqui
        var sharedPhone : String? = shareFich.getString(getString(R.string.placeholder), null)

        sharedPhone?.let {
            startActivity2(sharedPhone)
        }

        //Que cuando le demos al boton se compruebe si es valido el numero introducido
        confBing.confirmCambio.setOnClickListener {
            inicioDeLosShared()
            val numero = confBing.fieldForN.text.toString();
            Log.d("------------Comparacion--------------",numero)

            if (numero.isNotEmpty()) {
                if (isValidPhoneNumber2(numero, "ES")) {
                    reWritePhone(numero)
                    startActivity2(numero)

                } else {
                    Toast.makeText(this, "El numero no es valido", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "Que haces?", Toast.LENGTH_LONG).show()
            }
        }
        confBing.backFromPh.setOnClickListener {
            var intencion : Intent? = Intent(this, Activity::class.java)
            sharedPhone?.let { intencion = Intent(this, CallScreen::class.java) }

            intencion?.apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP) }

            startActivity(intencion)
        }
    }

    private fun inicioDeLosShared(){
        var nombreFich = getString(R.string.fitch_preferences)
        this.phoneSh = confBing.fieldForN.text.toString()
        Log.d("------------TEST--------------",phoneSh.toString())

        this.shareFich = getSharedPreferences(nombreFich, Context.MODE_PRIVATE)
    }

    private fun reWritePhone(phon : String?){
        shareFich.edit().putString(getString(R.string.placeholder), phon).apply()
        shareFich.getString(getString(R.string.placeholder),"")?.let { Log.d("--------------Recup-----------",it) }
    }

    private fun startActivity2(numero : String){
        var intencion = Intent(this, CallScreen::class.java)
            intencion.apply {
            putExtra(getString(R.string.placeholder), numero)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intencion)
        Log.d("Emergencia", "Me muestro despues del startAct")
    }


    fun isValidPhoneNumber2(phoneNumber: String, countryCode: String): Boolean {
        val phoneUtil = PhoneNumberUtil.getInstance()
        return try {
            // Parseamos el número de teléfono en base al código de país proporcionado
            val number = phoneUtil.parse(phoneNumber, countryCode)
            // Verificamos si es un número válido para el país especificado
            phoneUtil.isValidNumber(number)
        } catch (e: NumberParseException) {
            e.printStackTrace()
            false
        }
    }

    override fun onNewIntent(intent: Intent) { //Tengo que comprender como funciona
        super.onNewIntent(intent)
        setIntent(intent)
    }






}