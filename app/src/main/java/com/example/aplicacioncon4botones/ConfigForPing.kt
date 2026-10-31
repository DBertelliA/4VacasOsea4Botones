package com.example.aplicacioncon4botones

import android.content.Intent
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri


class ConfigForPing(private var act : Activity) {
    public var textoIn : String = ""


    fun tarjetaDeUbi(){
        var campoTexto = EditText(act).apply {
            hint = "Pon lo que desees"
        }

        AlertDialog.Builder(act).setTitle("Google maps")
            .setView(campoTexto)
            .setPositiveButton("Aceptar"){ _,_ /*Esos son argumentos vacios para ignorar el error de parametros*/->
                textoIn = campoTexto.text.toString()
                funcionalidadPin(textoIn.trim())
            }
            .setNegativeButton("Cancelar", null)
            .show()

    }

    fun funcionalidadPin(pin : String){
        try {
            val intencion = Intent(
                Intent.ACTION_VIEW,
                ("geo:0,0?q=$pin").toUri()
            )
            act.startActivity(intencion)
        }catch (e: Exception){
            act.toastMaker("???")
            e.message?.let { msg -> Log.d("//Error//", msg) }
        }
    }



}