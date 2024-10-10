package com.example.androidnewsetup.data.remote

import com.example.androidnewsetup.data.bean.ApiResponse
import com.example.androidnewsetup.data.bean.request.Request
import com.example.androidnewsetup.data.bean.user.UserBean
import com.example.androidnewsetup.data.EndPoints
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    /**
     * Auth flow...
     */
    @POST(EndPoints.Auth.LOGIN)
    fun loginAsync(@Body request: Request.LoginRequest): Deferred<Response<ApiResponse<UserBean>>>
}
