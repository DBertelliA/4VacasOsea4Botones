package com.example.aplicacioncon4botones

import android.content.Context
import android.content.SharedPreferences
import android.widget.EditText
import androidx.appcompat.app.AlertDialog


class ConfigForURL(private var act : Activity) {
    public var textoIn : String = ""


    fun tarjetaIntroductoria(){
        var campoTexto = EditText(act).apply {
            hint = "https//: ..."
        }

        AlertDialog.Builder(act).setTitle("Introduce la URL")
            .setView(campoTexto)
            .setPositiveButton("Aceptar"){ _,_ /*Esos son argumentos vacios para ignorar el error de parametros*/->
                textoIn = campoTexto.text.toString()
                act.funcionalidadUrl(textoIn.trim())
            }
            .setNegativeButton("Cancelar", null)
            .show()

    }



}