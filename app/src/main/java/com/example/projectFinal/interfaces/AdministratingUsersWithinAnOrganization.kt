package com.example.projectFinal.interfaces

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AdministratingUsersWithinAnOrganization {
    @Headers("Content-Type: application/json")
    @PUT("v1/organizations/{organization_id}/users/{user_id}/organization_roles/member")
    suspend fun postData(
        @Header("X-Auth-token") authToken: String,
        @Path("user_id") userId: String,
        @Path("organization_id") orderId: String
    ): Response<ResponseBody>
}