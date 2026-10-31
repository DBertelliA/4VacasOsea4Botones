package com.example.aplicacioncon4botones

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacioncon4botones.databinding.ActivityMainBinding
import com.bumptech.glide.Glide

/**
 * Esta seccion de la aplicacion es utilizado para la llamada de metodos
 * que la interfaz necesita, constando de 4 simples botones los cuales tienen distintas funcionalidades
 * 1. Botón llamada tiene la funcion de llamar a un numero el cual nosotros le asignaremos en un menú que saldrá al persionarlo
 * 2. Botón de seteo de alarma a dos minutos
 * 3. Botón url, al presionarlo, saldra una tarjeta que capturará lo que le escribas, convirtiendolo en una url si no tiene su estructura o autocompletandolo para que no genere error
 * 4. Botón ubicacion, Parecido al anterior, es como el buscador de la ubicacion del google maps, el cual con poner cualquier cosa, te llevará a dicha cosa
 * @author Daniel
 * @Version 1.0
 */

class Activity : AppCompatActivity() {
    private lateinit var configBinding : ActivityMainBinding

    private var unidades = Calendar.getInstance()

    private var classForFrameURL : ConfigForURL = ConfigForURL(this)

    private var classForFramePin : ConfigForPing = ConfigForPing(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(configBinding.root)


        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Glide.with(this).asGif().load(R.drawable.spamtom).into(configBinding.imageView)
        onStart()
    }

    /**
     * En el metodo onStart, se asignan las funciones a todos los botones mas no la logica
     */

    override fun onStart() {
        super.onStart()

        configBinding.callButton.setOnClickListener {
            val intencion = Intent(this, ConfigForCall::class.java)
                intencion.apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
            startActivity(intencion)
        }


        configBinding.URLbutton.setOnClickListener {
            classForFrameURL.tarjetaIntroductoria()
        }

        configBinding.alarmButton.setOnClickListener {
            try {
                var mins = unidades.get(java.util.Calendar.MINUTE)
                val intencion =
                    Intent(AlarmClock.ACTION_SET_ALARM).apply {
                        putExtra(AlarmClock.EXTRA_HOUR, unidades.get(java.util.Calendar.HOUR_OF_DAY))
                        putExtra(AlarmClock.EXTRA_MINUTES, mins + 2)
                        putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                    }

                startActivity(intencion)
            }catch (e: Exception){
                toastMaker(e.message)
            }
        }

        configBinding.ubicationButton.setOnClickListener {
            classForFramePin.tarjetaDeUbi()
        }


    }

    fun toastMaker(message : String?){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}