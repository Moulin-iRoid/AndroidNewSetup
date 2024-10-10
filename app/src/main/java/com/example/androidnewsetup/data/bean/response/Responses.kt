package com.example.androidnewsetup.data.bean.response

import com.example.androidnewsetup.data.Constants
import com.example.androidnewsetup.data.bean.user.Authentication
import com.google.gson.annotations.SerializedName

class Responses {
    data class AuthCommonResponse(
        @SerializedName(Constants.RequestParameters.AUTH) var auth: Authentication = Authentication()
    )
}