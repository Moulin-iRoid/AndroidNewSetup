package com.example.androidnewsetup.data.bean

import com.example.androidnewsetup.data.bean.pagingcommondata.Meta
import com.google.gson.annotations.SerializedName


open class ApiResponse<Any>(
    @SerializedName("data") var data: Any? = null,
    @SerializedName("message") val message: String = "",
    @SerializedName("errors") var apiErrors: ApiErrors? = null
)

data class ApiErrors(
    val email: ArrayList<String> = ArrayList(),
    val first_name: ArrayList<String> = ArrayList(),
    val last_name: ArrayList<String> = ArrayList(),
    val password: ArrayList<String> = ArrayList(),
    val confirm_password: ArrayList<String> = ArrayList(),
    val phone_number: ArrayList<String> = ArrayList(),
    val role: ArrayList<String> = ArrayList(),
    val username: ArrayList<String> = ArrayList()
)

open class ApiResponseNew<Any>(
    @SerializedName("data") val data: List<Any> = listOf(),
    @SerializedName("message") val message: String = "",
    @SerializedName("different_licensor") val differentLicensor: Int = -1,
    @SerializedName("error") val errorMsg: String = "",
    @SerializedName("errors") var apiErrors: ApiErrors? = null,
    @SerializedName("meta") val meta: Meta
)

