package com.example.vinillos_app_misw.data.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


/*
    * Adaptador que permite leer o escribir informacion del Back-End.
    *
    * Params:
    ** Context ->
*/
class VolleyBroker constructor(context: Context){

    /*
        * Instancia que permite la comunicación de la aplicacion con el API del
        * Back-End por medio de la implementación del API de Volley
    */
    val instance: RequestQueue = Volley.newRequestQueue(context.applicationContext)

    companion object{
        /*
           * Direccion de consumo de API corriendo en el servidor.
        */
         const val BASE_URL= "http://34.216.229.151:3000/"
        /*
          * Direccion de consumo de API corriendo en el local..
        */
        // const val BASE_URL= "https://localhost:3000/"

        /*
          * getRequest: Metodo que permite solicitar peticiones Get al Back-End
          *
          * Params:
          * * path -> Complemento de la dirección del API para solicitar información de un servicio
          * en especifico.
          *
          * * responseListener -> Observable que retorna la respuesta exitosa de la petición.
          *
          * * errorListener -> Observable que retorna la respuesta fallida de la petición.
        */
        fun getRequest(
            path:String,
            responseListener: Response.Listener<String>,
            errorListener: Response.ErrorListener,
            ): StringRequest {
            return StringRequest(Request.Method.GET, BASE_URL +path, responseListener,errorListener)
        }

        /*
          * postRequest: Metodo que permite enviar peticiones Post al Back-End
          *
          * Params:
          * * path -> Complemento de la dirección del API para solicitar escribir información de un servicio
          * en especifico.
          *
          * * body -> Información de escritura en formato de JSON.
          *
          * * responseListener -> Observable que retorna la respuesta exitosa de la petición.
          *
          * * errorListener -> Observable que retorna la respuesta fallida de la petición.
        */
        fun postRequest(
            path: String,
            body: JSONObject,
            responseListener: Response.Listener<JSONObject>,
            errorListener: Response.ErrorListener,
            ):JsonObjectRequest{
            return  JsonObjectRequest(Request.Method.POST, BASE_URL +path, body, responseListener, errorListener)
        }

    }

}