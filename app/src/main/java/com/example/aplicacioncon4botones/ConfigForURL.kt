package com.example.aplicacioncon4botones

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
                textoIn = textoIn.trim()
                var valor = textoIn
                if (!(textoIn.contains("https://"))){
                    valor = "https://${textoIn}"
                    if(!(textoIn.contains(".com"))) {
                        valor = "https://${textoIn}.com"
                    }
                }

                if (!(textoIn.contains(".com"))){
                    valor = "${textoIn}.com";
                    if(!(textoIn.contains("https://"))) {
                        valor = "https://${textoIn}.com"
                    }
                }
                act.funcionalidadUrl(valor)
            }
            .setNegativeButton("Cancelar", null)
            .show()

    }



}