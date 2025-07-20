package com.example.x_project_android.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.x_project_android.R
import com.example.x_project_android.data.dto.UserRegistrationDto
import com.example.x_project_android.repositories.RegisterRepository
import com.example.x_project_android.utils.byteArrayToRequestBody
import com.example.x_project_android.utils.uriToByteArray
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

fun String.isEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.toRequestBody() = this.toRequestBody("text/plain".toMediaTypeOrNull())

class RegisterViewModel(
    private val registerRepository: RegisterRepository = RegisterRepository()
) : ViewModel() {

    private val _uiEvent = Channel<RegisterUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var _bio = mutableStateOf("")
    val bio: State<String> get() = _bio

    private var _pseudo = mutableStateOf("")
    val pseudo: State<String> get() = _pseudo

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> get() = _imageUri

    private var _email = mutableStateOf("")
    val email: State<String> get() = _email

    private var _password = mutableStateOf("")
    val password: State<String> get() = _password

    private var _confirmPassword = mutableStateOf("")
    val confirmPassword: State<String> get() = _confirmPassword

    fun setLoading(state : Boolean){
        _isLoading.value = state
    }

    fun setBio(newBio: String) {
        if (newBio.length <= 250) {
            _bio.value = newBio
        }
    }
    fun setPseudo(newPseudo: String) {
        if (newPseudo.length <= 25) {
            _pseudo.value = newPseudo
        }
    }
    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun setEmail(newEmail: String) {
        if (newEmail.length <= 1000) {
            _email.value = newEmail
        }
    }
    fun setPassword(newPassword: String) {
        if (newPassword.length <= 1000) {
            _password.value = newPassword
        }
    }
    fun setConfirmPassword(newConfirmPassword: String) {
        if (newConfirmPassword.length <= 1000) {
            _confirmPassword.value = newConfirmPassword
        }
    }

    fun goToPassword(){
        if(_email.value.isNotBlank() && _email.value.isEmail()){
            _uiEvent.trySend(RegisterUIEvent.NavigateTo)
        }
        else if(_email.value.isBlank()){
            _uiEvent.trySend(RegisterUIEvent.ShowError(R.string.registerviewmodel_error_email_empty))
        }
        else{
            _uiEvent.trySend(RegisterUIEvent.ShowError(R.string.registerviewmodel_error_wrongly_format))
        }
    }

    fun goToImage() {
        if (_pseudo.value.isNotBlank() && _bio.value.isNotBlank()) {
            _uiEvent.trySend(RegisterUIEvent.NavigateTo)
        } else {
            _uiEvent.trySend(RegisterUIEvent.ShowError(R.string.registerviewmodel_error))
        }
    }

    fun goToEmail() {
        if (_imageUri.value != null) {
            _uiEvent.trySend(RegisterUIEvent.NavigateTo)
        } else {
            _uiEvent.trySend(RegisterUIEvent.ShowError(R.string.registerviewmodel_error_no_image))
        }
    }
    
    fun tryRegister(context : Context){
        if(_password.value.isBlank() || _confirmPassword.value.isBlank()){
            _uiEvent.trySend(RegisterUIEvent.ShowError(R.string.registerviewmodel_error_passwords_empty))
            return
        }
        else if(_password.value != _confirmPassword.value){
            _uiEvent.trySend(RegisterUIEvent.ShowError(R.string.registerviewmodel_error_password_unmatch))
            return
        }
        val bytes = uriToByteArray(context, _imageUri.value ?: return)
        if(bytes == null){
            _uiEvent.trySend(RegisterUIEvent.ShowError(R.string.an_error_occurred_while_reading_the_image))
            return
        }
        val profilePicture = byteArrayToRequestBody(bytes, "image/jpeg")

        val userRegister = UserRegistrationDto(
            bio = _bio.value,
            pseudo = _pseudo.value,
            email = _email.value,
            password = _password.value
        )
        registerRepository.postRegister(userRegister,profilePicture){result ->
            when(result){
                is RegisterResult.Success -> {
                    _uiEvent.trySend(RegisterUIEvent.NavigateTo)
                    _isLoading.value = false
                }
                is RegisterResult.Failure -> {
                    _uiEvent.trySend(RegisterUIEvent.ShowError(result.message))
                    _isLoading.value = false
                }
            }
        }
        _isLoading.value = true
    }
}

sealed class RegisterResult {
    data class Success(val message: Int) : RegisterResult()
    data class Failure(val message: Int) : RegisterResult()
}


sealed class RegisterUIEvent {
    data object NavigateTo : RegisterUIEvent()
    data class ShowError(val message: Int) : RegisterUIEvent()
}