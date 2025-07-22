package com.example.x_project_android.tokenStore

object TokenManager {
    private var token: String? = null

    fun saveToken(newToken: String) {
        token = "Bearer $newToken"
    }

    fun getToken(): String? = token

    fun clearToken() {
        token = null
    }
}