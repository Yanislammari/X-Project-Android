package com.example.x_project_android.networking.services

import com.example.x_project_android.data.dto.UserRegistrationResponseDto
import retrofit2.http.POST
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.Part

interface RegisterService {
    @Multipart
    @POST("auth/register")
    fun registerUser(
        @Part("bio") bio: RequestBody,
        @Part("pseudo") pseudo: RequestBody,
        @Part profilePicture: MultipartBody.Part?,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
    ): Call<UserRegistrationResponseDto>
}