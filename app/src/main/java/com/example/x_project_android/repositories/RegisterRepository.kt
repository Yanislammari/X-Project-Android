package com.example.x_project_android.repositories

import android.util.Log
import com.example.x_project_android.R
import com.example.x_project_android.data.dto.UserRegistrationDto
import com.example.x_project_android.data.dto.UserRegistrationResponseDto
import com.example.x_project_android.networking.RetrofitHttpClient
import com.example.x_project_android.networking.services.RegisterService
import com.example.x_project_android.tokenStore.TokenManager
import com.example.x_project_android.viewmodels.RegisterResult
import com.example.x_project_android.viewmodels.toRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Callback

class RegisterRepository {
    private val registerService = RetrofitHttpClient.instance.create(RegisterService::class.java)

    fun postRegister(
        user : UserRegistrationDto,
        profilePicture : RequestBody,
        callback: (RegisterResult) -> Unit
    ) {
        val bio = user.bio.toRequestBody()
        val pseudo = user.pseudo.toRequestBody()
        val email = user.email.toRequestBody()
        val password = user.password.toRequestBody()

        val fileImage = MultipartBody.Part.createFormData(
            name = "profilePicture", // field name expected by your backend
            filename = "image.jpg",  // any name works, real filename optional
            body = profilePicture
        )

        registerService.registerUser(
            bio = bio,
            pseudo = pseudo,
            profilePicture = fileImage,
            email = email,
            password = password
        ).enqueue(object : Callback<UserRegistrationResponseDto> {
            override fun onResponse(call: retrofit2.Call<UserRegistrationResponseDto>, response: retrofit2.Response<UserRegistrationResponseDto>) {
                if (response.isSuccessful && response.code() == 201) {
                    val userResponse = response.body()
                    userResponse?.let {
                        if(it.token != null) {
                            TokenManager.saveToken(it.token)
                            callback(RegisterResult.Success(R.string.registration_successful_token_saved))
                        } else {
                            callback(RegisterResult.Failure(R.string.an_error_occurred_during_registration))
                        }
                    }
                } else {
                    callback(RegisterResult.Failure(R.string.an_error_occurred_during_registration))
                }
            }

            override fun onFailure(call: retrofit2.Call<UserRegistrationResponseDto>, t: Throwable) {
                callback(RegisterResult.Failure(R.string.request_failed_please_try_again_later))
            }
        })
    }
}