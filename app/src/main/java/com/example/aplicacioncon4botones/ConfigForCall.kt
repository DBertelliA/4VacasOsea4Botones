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

/**
 * Clase que contiene las funciones de configuracion del numero de telefono usado en la clase principal CallScreen
 * Contiene:
 * 1. Un metodos para almacenar y comprobar los numeros antes de enviarlos
 * 2. Iniciacion de los ficheros compartidos
 *
 * @author Daniel
 * @Version
 */
class ConfigForCall : AppCompatActivity() {
    private lateinit var confBing : ChangePhoneBinding
    private lateinit var shareFich : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        confBing = ChangePhoneBinding.inflate(layoutInflater)
        setContentView(confBing.root)

        enableEdgeToEdge()

        inicioDeLosShared()
        start()
    }

    /**
     * EL onResume actua como puente y comprobante de condiciones necesarias para las funciones de la clase
     * En caso de venir del menú principal por primera vez o del menu de llamada, se ejecuta una serie
     * controles que ayudan a saber si a lo que viene el usuario es o a iniciarlo o a reiniciar el numero
     */
    override fun onResume() {
        super.onResume()
        var reten = intent.getBooleanExtra("back", false)

        if(reten){
            confBing.fieldForN.setText("")
            toastMak("Introduce el numero")

            intent.removeExtra("back")
        }

    }


    private fun start() {

        //De aqui se coger un numero que supuestamente debe de venir vacio (gracias al remove de la otra clase)
        var sharedPhone : String? = shareFich.getString(getString(R.string.placeholder), null)

        //Si por lo que sea aún contiene un numero, se envia directamente a la clase de la llamda principal con
        //el numero encontrado
        sharedPhone?.let {
            startActivity2(sharedPhone)
        }
        //En caso contrario no lo hace, y se nos permite usar sus funciones de cambio numero

        confBing.confirmCambio.setOnClickListener {
            val numero = confBing.fieldForN.text.toString();
            Log.d("------------Comparacion--------------",numero)

            if (numero.isNotEmpty()) {
                //Si la casilla de introduccion no está vacia, se llama a un metodo que valida los numero sprocedentes de distintos paises
                //En caso de no validarse solo te mostrará un Toast de numero invalido
                if (isValidPhoneNumber2(numero, "ES")) {
                    reWritePhone(numero)
                    startActivity2(numero)

                } else {
                    toastMak("El numero no es valido")
                }
            }else{
                toastMak("Que haces?")
            }
        }
        //Este es para la salida del menú de inicio o el menu del tefefono dependiendo de si hay un numero o no compartido
        //(Pensadolo ahora no tiene mucho sentido pero lo dejo por si las dudas)
        confBing.backFromPh.setOnClickListener {
            var intencion : Intent? = Intent(this, Activity::class.java)
            sharedPhone?.let { intencion = Intent(this, CallScreen::class.java) }

            intencion?.apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP) }

            startActivity(intencion)
        }
    }

    private fun toastMak(mensaje : String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    //Esto incia los ficheros compartidos (Sin misterio)
    private fun inicioDeLosShared(){
        var nombreFich = getString(R.string.fitch_preferences)
        this.shareFich = getSharedPreferences(nombreFich, Context.MODE_PRIVATE)
    }

    //Este metodo es para ahorrar codigo y solo es la funcion para implementar la escritura del telefono en el fichero compartido
    private fun reWritePhone(phon : String?){
        shareFich.edit().putString(getString(R.string.placeholder), phon).apply()
        shareFich.getString(getString(R.string.placeholder),"")?.let { Log.d("--------------Recup-----------",it) }
    }

    //Este startActivity permite la pasada de valores de una clase a otra
    private fun startActivity2(numero : String){
        var intencion = Intent(this, CallScreen::class.java)
            intencion.apply {
            putExtra(getString(R.string.placeholder), numero)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intencion)
        //Log.d("Emergencia", "Me muestro despues del startAct")
    }

    //Este es el metodo que valida los numeros (Codigo reutilizado)
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }






}