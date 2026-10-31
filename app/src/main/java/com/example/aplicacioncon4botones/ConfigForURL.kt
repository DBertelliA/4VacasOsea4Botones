package com.example.aplicacioncon4botones

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

/**
 * Esta clase es dedicada a la generacion de una tarjeta para capturar texto que se introducirá en el buscador para luego de darle al boton de internet o url
 * Contiene el esquelo de la contruccion de una tarjeta, junto con la funcion de un intent para la apertura del navegador y ajustes para generar un esqueleto de url automatico (no es perfecto)
 *
 * @param Activity (mainActivity)
 * @author Daniel
 * @Version 1.0
 */


class ConfigForURL(private var act : Activity) {
    public var textoIn : String = ""

    /**
     * Este metodo tiene la funcion de invocar a la tarjeta de captura de texto
     * Esta no para la aplicacion, por lo que debemos setear la funcion una vez se le dá al boton aceptar
     */
    fun tarjetaIntroductoria(){
        var campoTexto = EditText(act).apply {
            hint = "https//: ..."
        }
        /**Una vez creado, se crea la tarjeta, el cual es un alertDialog
         *Se le pone texto, se le referencia la instancia, y a la hora de setear
         *lo que va a hacer el boton aceptar, por defecto, esta debe de recibir dos variables (no entendía el porque en la documentacion de android)
         *Sin embargo, podemos saltarnos esta condición con tan solo dos "_", que significan literalmente nada
         **/
        AlertDialog.Builder(act).setTitle("Introduce la URL")
            .setView(campoTexto)
            .setPositiveButton("Aceptar"){ _,_ /*Esos son argumentos vacios para ignorar el error de parametros*/->
                //Esta partye es para pillar el texto pasado y añadirle un esquelo mas de acorde a lo que le falta
                //Por ejemplo, si pones google, el formato para que el intent funcione debe de ser https://google.com
                //pues esta parte se lo pone si ve que no lo contiene para intentar evitar el fallo, aunque eso no evitar que puedas forzar dicho fallo
                //introduciendo cosas que el buscador no le gusta
                textoIn = campoTexto.text.toString()
                textoIn = textoIn.trim()
                var valor = textoIn
                if(valor.isNotEmpty()){
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
                }
                funcionalidadUrl(valor)
            }
            .setNegativeButton("Cancelar", null)
            .show()

    }

    /**
     * Solo con action view ya estamos entrado al navegador, y una vez se le pone un texto, es como si ubieramos puesto algo en el navegador
     */
    fun funcionalidadUrl(url : String){
        try {
            val intencion = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            act.startActivity(intencion)
        }catch (e: Exception){
            act.toastMaker("URL invalido")
            e.message?.let { msg -> Log.d("//Error//", msg) }
        }
    }



}