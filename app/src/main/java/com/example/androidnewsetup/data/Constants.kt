package com.example.androidnewsetup.data

object Constants {
    const val PER_PAGE = 20

    object RequestParameters {
        const val MULTI_PART_FORM: String = "multipart/form-data"
        const val FULL_NAME: String = "fullName"
        const val EMAIL: String = "email"
        const val FLAT_NO: String = "flatNo"
        const val LATITUDE: String = "latitude"
        const val LONGITUDE: String = "longitude"
        const val COUNTRY_CODE: String = "countryCode"
        const val COUNTRY_NAME: String = "countryName"
        const val PHONE: String = "phone"
        const val DOB: String = "dob"
        const val EMERGENCY_COUNTRY_CODE: String = "emergencyCountryCode"
        const val EMERGENCY_COUNTRY_NAME: String = "emergencyCountryName"
        const val EMERGENCY_PHONE: String = "emergencyPhone"
        const val PASSWORD: String = "password"
        const val PROFILE_IMAGE: String = "profileImage"
        const val ID: String = "_id"
        const val IS_PHONE_VERIFY: String = "isPhoneVerified"
        const val AUTH: String = "auth"
        const val ACCESS_TOKEN: String = "accessToken"
        const val REFRESH_TOKEN: String = "refreshToken"
        const val TOKEN_TYPE: String = "tokenType"
        const val EXPIRES_IN: String = "expiresIn"
    }

    object Broadcast {
        const val ID: String = "id"
    }

    object AppInfo {
        const val DIR_NAME = "AndroidSetupMedia"
        const val FILE_PREFIX_NAME = "AndroidSetupMedia_"
    }

    object Common {
        const val USER_DATA: String = "userData"
        const val AUTH_DATA: String = "authData"
    }
}
