package com.example.aplicacioncon4botones

import android.content.Intent
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri

/**
 * Esta clase es dedicada a la generacion de una trjeta para capturar texto que se introducirá en el google maps luego de darle al boton de ubicacion
 * Contiene el esquelo de la contruccion de una tarjeta, junto con la funcion de un intent para la apertura de google maps y pasada de informacion
 *
 * @param Activity (mainActivity)
 * @author Daniel
 * @Version 1.0
 */
class ConfigForPing(private var act : Activity) {
    public var textoIn : String = ""

    /**
     * Este metodo tiene la funcion de invocar a la tarjeta de captura de texto
     * Esta no para la aplicacion, por lo que debemos setear la funcion una vez se le dá al boton aceptar
     */
    fun tarjetaDeUbi(){
        //Primero se crea la variable que va a contener el texto
        var campoTexto = EditText(act).apply {
            hint = "Pon lo que quieras"
        }

        /**Una vez creado, se crea la tarjeta, es cual es un alertDialog
        *Se le pone texto, se le referencia la instancia, y a la hora de setear
        *lo que va a hacer el boton aceptar, por defecto, esta debe de recibir dos variableS (no entendía el porque en la documentacion de android)
        *Sin embargo, podemos saltarnos esta condición con tan solo dos "_", que significan literalmente nada
        **/
        AlertDialog.Builder(act).setTitle("Google maps")
            .setView(campoTexto)
            .setPositiveButton("Aceptar"){ _,_ /*Esos son argumentos vacios para ignorar el error de parametros*/->
                textoIn = campoTexto.text.toString()
                funcionalidadPin(textoIn.trim())
            }
            .setNegativeButton("Cancelar", null)
            .show()

    }

    /**
     * La forma en la que llamamos al google maps, ya no es con el comando Action_view, sino que
     * en el uri, debemos poner "geo:0,0?=${args}"
     */
    fun funcionalidadPin(pin : String){
        try {
            val intencion = Intent(
                Intent.ACTION_VIEW,
                ("geo:0,0?q=$pin").toUri()
            )
            act.startActivity(intencion)
        }catch (e: Exception){
            //Esto no deberia saltar
            act.toastMaker("???")
            e.message?.let { msg -> Log.d("//Error//", msg) }
        }
    }



}