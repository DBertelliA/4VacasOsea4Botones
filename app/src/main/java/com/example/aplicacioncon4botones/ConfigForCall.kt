package com.example.aplicacioncon4botones

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import com.example.aplicacioncon4botones.databinding.ChangePhoneBinding
import java.util.zip.Inflater
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
            Toast.makeText(this, "Introduce el numero", Toast.LENGTH_LONG)

            intent.removeExtra("back")
        }

    }


    private fun start() {

        //Que resete el numero de la carpeta cada que se entre a aqui
        var sharedPhone : String? = shareFich.getString(phoneSh, null)

        sharedPhone?.let {
            startActivity(sharedPhone)
        }

        //Que cuando le demos al boton se compruebe si es valido el numero introducido
        confBing.confirmCambio.setOnClickListener {
            val numero = confBing.fieldForN.text.toString();

            if (numero.isNotEmpty()) {
                if (isValidPhoneNumber2(numero, "ES")) {
                    reWritePhone(numero)
                    startActivity(numero)

                } else {
                    Toast.makeText(this, "El numero no es valido", Toast.LENGTH_LONG)
                }
            }else{
                Toast.makeText(this, "Que haces?", Toast.LENGTH_LONG)
            }
        }
        // y que llamemos al nuevo intent pasandole el nuevo numero
    }

    private fun inicioDeLosShared(){
        var nombreFich = getString(R.string.fitch_preferences)
        this.phoneSh = getString(R.string.introduce)

        this.shareFich = getSharedPreferences(nombreFich, Context.MODE_PRIVATE)
    }

    private fun reWritePhone(phon : String?){ shareFich.edit().putString(phoneSh, phon).apply() }

    private fun startActivity(numero : String){
        var intencion = Intent(this, CallScreen::class.java)
            intencion.apply {
            putExtra(getString(R.string.placeholder), numero)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intencion)
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