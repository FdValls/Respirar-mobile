package com.example.projectFinal.data

import android.app.Application
import com.example.projectFinal.utils.*
import com.google.gson.JsonArray

class GlobalVariables : Application() {

    var myXSubjectToken: String = ""
    var url = "http://ip172-19-0-17-ciccmfufml8g00denm6g-3000.direct.labs.play-with-docker.com/"
    var listUsers = mutableListOf<UserDto>()
    var listOrganizationsForUpdate = mutableListOf<Organization>()
    var userData: String = ""
    var listOrgToModify = mutableSetOf<String>()
    var myArrayOrgJson : JsonArray = JsonArray()

    companion object {
        private lateinit var instance: GlobalVariables

        fun getInstance(): GlobalVariables {
            if (!Companion::instance.isInitialized) {
                instance = GlobalVariables()
            }
            return instance
        }
    }
}