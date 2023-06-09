package com.example.projectFinal.endPoints.RequestOrganizations

import ar.edu.ort.requestexamples.data.TrustAllCerts
import com.example.projectFinal.data.GlobalVariables
import com.example.projectFinal.interfaces.ReadInfoOrganization
import com.example.projectFinal.utils.Organization
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestReadOrganizationDetails {

    private lateinit var myReturn: Organization

    suspend fun sendRequest(id: String) {

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

        val apiService = retrofit.create(ReadInfoOrganization::class.java)

        val response = apiService.getOrgById(GlobalVariables.getInstance().myXSubjectToken, id)

        println("RESPONSE RequestReadOrganizationDetails: $response")
        println("Body RequestReadOrganizationDetails: ${response.body()}")

        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                val responseBodyString = responseBody.string()

                //Parsear a JSON para obtener los datos
                val gson = Gson()
                val jsonObject = gson.fromJson(responseBodyString, JsonObject::class.java)
                val userObject = jsonObject.getAsJsonObject("organization")
//                myReturn = Organization(
//                    userObject.get("id").asString,
//                    userObject.get("name").asString,
//                    userObject.get("description").asString,
//                    userObject.get("image").asString,
//                    userObject.get("website")?.asString,
//                )
                println("El valor de 'userObject' es: $userObject")
                val description = userObject?.get("description")?.asString
                description?.let {
                    println("El valor de 'description' es: $it")
                }
                val name = userObject?.get("name")?.asString
                name?.let {
                    println("El valor de 'name' es: $it")
                }

            } else {
                println("Empty response body")
            }
        } else {
            println("Request failed: ${response.code()}")
        }
//        return myReturn
    }
}