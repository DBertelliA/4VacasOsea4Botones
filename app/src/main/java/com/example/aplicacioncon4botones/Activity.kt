package com.example.aplicacioncon4botones

import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacioncon4botones.databinding.ActivityMainBinding
import androidx.core.net.toUri

class Activity : AppCompatActivity() {
    private lateinit var configBinding : ActivityMainBinding

    private var unidades = Calendar.getInstance()

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
        onStart()
    }

    override fun onStart() {
        super.onStart()

        configBinding.callButton.setOnClickListener {
            val intencion = Intent(this, CallScreen::class.java)
                intencion.apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
            startActivity(intencion)
        }

        configBinding.URLbutton.setOnClickListener {
            try {
                val intencion = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com")
                )
                startActivity(intencion)
            }catch (e: Exception){
                toastMaker(e.message)
            }
        }

        configBinding.alarmButton.setOnClickListener {
            try {
                var mins = unidades.get(java.util.Calendar.MINUTE)
                val intencion =
                    Intent(AlarmClock.ACTION_SET_ALARM).putExtra(AlarmClock.EXTRA_MINUTES, mins + 2)
                        .putExtra(AlarmClock.EXTRA_SKIP_UI, true)

                startActivity(intencion)
            }catch (e: Exception){
                toastMaker(e.message)
            }
        }

        configBinding.ubicationButton.setOnClickListener {
            val intencion = Intent(Intent.ACTION_VIEW, "geo:0,0?q=restaurantes".toUri())
            startActivity(intencion)
        }


    }

    fun toastMaker(message : String?){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}