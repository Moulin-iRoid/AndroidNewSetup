package com.example.androidnewsetup.data.remote.helper

import com.example.androidnewsetup.data.bean.ApiResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.File

object ApiUtils {

    //Convert api error body
    fun getAPIError(errorBody: ResponseBody?): String {
        return try {
            val apiRes: ApiResponse<Any> = ApiResponse()
            val apiResponse: ApiResponse<Any>? =
                Gson().fromJson(errorBody?.string(), apiRes::class.java)
            apiResponse?.message ?: ""
        } catch (e: Exception) {
            errorBody?.string() ?: ""
        }
    }

    //Convert files to multipart
    fun createMultipartBodyForImage(file: File?, keyName: String): MultipartBody.Part {
        return if (file != null) {
            MultipartBody.Part.createFormData(keyName, file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
        } else {
            MultipartBody.Part.createFormData(keyName, "", "".toRequestBody("text/plain".toMediaTypeOrNull()))
        }
    }

    fun createMultipartBodyForVideo(file: File?, keyName: String): MultipartBody.Part {
        return if (file != null) {
            MultipartBody.Part.createFormData(keyName, file.name, file.asRequestBody("video/*".toMediaTypeOrNull()))
        } else {
            MultipartBody.Part.createFormData(keyName, "", "".toRequestBody("text/plain".toMediaTypeOrNull()))
        }
    }

    fun createMultipartBodyForFile(file: File?, keyName: String): MultipartBody.Part {
        return if (file != null) {
            MultipartBody.Part.createFormData(keyName, file.name, file.asRequestBody("*/*".toMediaTypeOrNull()))
        } else {
            MultipartBody.Part.createFormData(keyName, "", "".toRequestBody("text/plain".toMediaTypeOrNull()))
        }
    }

    fun createMultipartBodyForDocument(file: File?, keyName: String): MultipartBody.Part {
        return if (file != null) {
            MultipartBody.Part.createFormData(keyName, file.name, file.asRequestBody("*/*".toMediaTypeOrNull()))
        } else {
            MultipartBody.Part.createFormData(keyName, "", "".toRequestBody("text/plain".toMediaTypeOrNull()))
        }
    }
}