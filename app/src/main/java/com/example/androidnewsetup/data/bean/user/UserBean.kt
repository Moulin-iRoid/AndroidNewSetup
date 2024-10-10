package com.example.androidnewsetup.data.bean.user

import com.google.gson.annotations.SerializedName
import com.example.androidnewsetup.data.Constants

data class UserBean(
    @SerializedName(Constants.RequestParameters.ID) var id: String = "",
    @SerializedName(Constants.RequestParameters.FULL_NAME) var fullName: String = "",
    @SerializedName(Constants.RequestParameters.PROFILE_IMAGE) var profileImage: String? = null,
    @SerializedName(Constants.RequestParameters.EMAIL) var email: String = "",
    @SerializedName(Constants.RequestParameters.DOB) var dob: Long = 0,
    @SerializedName(Constants.RequestParameters.COUNTRY_CODE) var countryCode: String = "",
    @SerializedName(Constants.RequestParameters.COUNTRY_NAME) var countryName: String = "",
    @SerializedName(Constants.RequestParameters.PHONE) var phone: String = "",
    @SerializedName(Constants.RequestParameters.EMERGENCY_COUNTRY_CODE) var emergencyCountryCode: String = "",
    @SerializedName(Constants.RequestParameters.EMERGENCY_COUNTRY_NAME) var emergencyCountryName: String = "",
    @SerializedName(Constants.RequestParameters.EMERGENCY_PHONE) var emergencyPhoneNumber: String = "",
    @SerializedName(Constants.RequestParameters.FLAT_NO) var flatNo: String = "",
    @SerializedName(Constants.RequestParameters.LATITUDE) val latitude: Double = 0.0,
    @SerializedName(Constants.RequestParameters.LONGITUDE) val longitude: Double = 0.0,
    @SerializedName(Constants.RequestParameters.IS_PHONE_VERIFY) var isPhoneVerified: Boolean = false,
    @SerializedName(Constants.RequestParameters.AUTH) var authentication: Authentication = Authentication()
)

data class Authentication(
    @SerializedName(Constants.RequestParameters.ACCESS_TOKEN) var accessToken: String = "",
    @SerializedName(Constants.RequestParameters.EXPIRES_IN) var expiresIn: Long = 0,
    @SerializedName(Constants.RequestParameters.REFRESH_TOKEN) var refreshToken: String = "",
    @SerializedName(Constants.RequestParameters.TOKEN_TYPE) var tokenType: String = ""
)