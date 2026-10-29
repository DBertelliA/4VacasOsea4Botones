package com.example.aplicacioncon4botones

import android.widget.EditText
import androidx.appcompat.app.AlertDialog


class ConfigForPing(private var act : Activity) {
    public var textoIn : String = ""


    fun tarjetaDeUbi(){
        var campoTexto = EditText(act).apply {
            hint = "Pon lo que desees"
        }

        AlertDialog.Builder(act).setTitle("Esto es como el buscador de la ubicacion")
            .setView(campoTexto)
            .setPositiveButton("Aceptar"){ _,_ /*Esos son argumentos vacios para ignorar el error de parametros*/->
                textoIn = campoTexto.text.toString()
                act.funcionalidadPin(textoIn.trim())
            }
            .setNegativeButton("Cancelar", null)
            .show()

    }



}