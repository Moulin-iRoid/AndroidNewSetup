package com.sbm.mobile.app.data.bean


import com.google.gson.annotations.SerializedName

data class MessageCommonResponse(
    @SerializedName("message")
    var message: String = ""
)