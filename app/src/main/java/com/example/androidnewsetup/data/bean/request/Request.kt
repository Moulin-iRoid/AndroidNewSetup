package com.example.androidnewsetup.data.bean.request

import com.google.gson.annotations.SerializedName
import com.example.androidnewsetup.data.Constants
import java.io.Serializable

class Request {

    data class LoginRequest(
        @SerializedName(Constants.RequestParameters.PHONE) var phone: Long?,
        @SerializedName(Constants.RequestParameters.COUNTRY_CODE) var countryCode: Int?,
        @SerializedName(Constants.RequestParameters.PASSWORD) var password: String?,
    ): Serializable {
        data class Builder(
            var phone: Long? = null,
            var countryCode: Int? = null,
            var password: String? = null
        ): Serializable {
            constructor(loginRequest: LoginRequest) : this (
                loginRequest.phone,
                loginRequest.countryCode,
                loginRequest.password
            )

            fun phone(phone: Long?) = apply { this.phone = phone }
            fun countryCode(countryCode: Int?) = apply { this.countryCode = countryCode }
            fun password(password: String?) = apply { this.password = password }

            fun build() = LoginRequest(phone, countryCode, password)
        }
    }
}