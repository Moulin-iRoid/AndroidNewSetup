package com.example.androidnewsetup.data.remote

import com.example.androidnewsetup.data.bean.ApiResponse
import com.example.androidnewsetup.data.bean.request.Request
import com.example.androidnewsetup.data.bean.user.UserBean
import com.example.androidnewsetup.data.remote.helper.ApiCallback
import com.example.androidnewsetup.data.remote.helper.ApiUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ApiRepositoryImpl(private val apiInterface: ApiInterface) : ApiRepository {
    override fun login(
        loginRequest: Request.LoginRequest, apiCallBack: ApiCallback<Response<ApiResponse<UserBean>>>
    ) {
        apiCallBack.onLoading()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.loginAsync(loginRequest).await()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            apiCallBack.onSuccess(response)
                        } else {
                            apiCallBack.onFailed(response.body()?.message ?: "")
                        }
                    } else {
                        val errorMsg = ApiUtils.getAPIError(response.errorBody())
                        apiCallBack.onFailed(errorMsg)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    apiCallBack.onErrorThrow(e)
                }
            }
        }
    }
}