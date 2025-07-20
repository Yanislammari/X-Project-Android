package com.example.x_project_android.repositories

import retrofit2.Callback
import com.example.x_project_android.data.dto.LoginDto
import com.example.x_project_android.data.dto.LoginResponseDto
import com.example.x_project_android.networking.RetrofitHttpClient
import com.example.x_project_android.networking.services.LoginService
import com.example.x_project_android.tokenStore.TokenManager
import com.example.x_project_android.viewmodels.LoginResult

class AuthRepository {
    private val loginService = RetrofitHttpClient.instance.create(LoginService::class.java)

    fun postLogin(email : String, password : String,callback: (LoginResult) -> Unit){
        val loginDto = LoginDto(email, password)
        loginService.login(loginDto).enqueue(object : Callback<LoginResponseDto> {
            override fun onResponse(call: retrofit2.Call<LoginResponseDto>, response: retrofit2.Response<LoginResponseDto>) {
                if (response.isSuccessful && response.code() == 200) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        if(it.token !=null){
                            TokenManager.saveToken(it.token)
                            callback(LoginResult.Success("Login successful, token saved."))
                        }
                    }
                    callback(LoginResult.Failure("Wrong credentials"))
                } else {
                    callback(LoginResult.Failure("Wrong credentials"))
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponseDto>, t: Throwable) {
                callback(LoginResult.Error("Request failed, please try again later."))
            }
        })
    }
}