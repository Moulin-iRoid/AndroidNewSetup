package com.example.androidnewsetup.data.remote

import com.example.androidnewsetup.data.bean.ApiResponse
import com.example.androidnewsetup.data.bean.request.Request
import com.example.androidnewsetup.data.bean.user.UserBean
import com.example.androidnewsetup.data.remote.helper.ApiCallback
import retrofit2.Response

interface ApiRepository {
    /**
     * Auth Flow...
     */
    fun login(loginRequest: Request.LoginRequest, apiCallBack: ApiCallback<Response<ApiResponse<UserBean>>>)
}