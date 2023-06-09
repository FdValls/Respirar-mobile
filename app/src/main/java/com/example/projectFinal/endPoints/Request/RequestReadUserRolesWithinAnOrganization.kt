package com.example.projectFinal.endPoints.Request

import ar.edu.ort.requestexamples.data.TrustAllCerts
import com.example.projectFinal.data.GlobalVariables
import com.example.projectFinal.endPoints.RequestOrganizations.RequestCreateOrganization
import com.example.projectFinal.interfaces.ReadUserRolesWithinAnOrganization
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestReadUserRolesWithinAnOrganization {
    var code: String = ""
    var role: String = ""
    var value = ""

    suspend fun sendRequest(idUser: String, idOrg: String) {

        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .hostnameVerifier { _, _ -> true }
            .sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), TrustAllCerts)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(GlobalVariables.getInstance().url) // Reemplaza con la URL de tu endpoint local
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val apiService = retrofit.create(ReadUserRolesWithinAnOrganization::class.java)
        val authToken = GlobalVariables.getInstance().myXSubjectToken

        val response = apiService.postData(authToken, idUser, idOrg)

        code = response.code().toString()

        println("Method PUT RequestReadUserRolesWithinAnOrganization")
        if (response.isSuccessful) {
            val responseBody = response.body()
            code = response.code().toString()
            val jsonBody = responseBody?.string()

            //Parsear a JSON para obtener los datos
            val gson = Gson()
            val jsonObject = gson.fromJson(jsonBody, JsonObject::class.java)
            val orgObject = jsonObject.getAsJsonObject("organization_user")

            role = orgObject.get("role").asString
            println("ROLE organizacion: $role")

            println("Body RequestReadUserRolesWithinAnOrganization: $jsonBody")

            println("RESPONSE RequestReadUserRolesWithinAnOrganization: $response")

            if (responseBody != null) {
                println("Code RequestReadUserRolesWithinAnOrganization: $code")
            } else {
                println("Request failed: ${response.code()}")
            }
        }
    }

    fun returnCode(): String{
        return code
    }

    fun returnRole(): String{
        return role
    }
}