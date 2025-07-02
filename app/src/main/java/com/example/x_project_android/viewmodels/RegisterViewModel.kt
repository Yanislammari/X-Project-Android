package com.example.x_project_android.viewmodels

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.x_project_android.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

fun String.isEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

class RegisterViewModel : ViewModel() {

    private val _uiEvent = Channel<RegisterUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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
    
    fun tryRegister(){
        if(_password.value.isBlank() || _confirmPassword.value.isBlank()){
            _uiEvent.trySend(RegisterUIEvent.ShowError(R.string.registerviewmodel_error_passwords_empty))
        }
        else if(_password.value != _confirmPassword.value){
            _uiEvent.trySend(RegisterUIEvent.ShowError(R.string.registerviewmodel_error_password_unmatch))
        }
        _uiEvent.trySend(RegisterUIEvent.NavigateTo)
    }
}

sealed class RegisterUIEvent {
    data object NavigateTo : RegisterUIEvent()
    data class ShowError(val message: Int) : RegisterUIEvent()
}