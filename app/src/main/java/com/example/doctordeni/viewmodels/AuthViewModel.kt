package com.example.doctordeni.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.doctordeni.models.requests.SignUpRequest
import com.example.doctordeni.models.requests.SigninRequest
import com.example.doctordeni.services.AccountService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val accountService: AccountService
) : ViewModel() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.IO)

    val signinEmail = mutableStateOf("")
    val signinPassword = mutableStateOf("")

    val regUsername = mutableStateOf("")
    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")
    val phoneNumber = mutableStateOf("")
    val address = mutableStateOf("")
    val postCode = mutableStateOf("")
    val regEmail = mutableStateOf("")
    val password = mutableStateOf("")
    val password1 = mutableStateOf("")


    private val _successfulAuthenticationActivity = MutableStateFlow<Boolean>(false)
    val succesfulAuthenticationActivity: StateFlow<Boolean> =
        _successfulAuthenticationActivity.asStateFlow()

    private val _errorInAuthenticationServic = MutableStateFlow<String>("")
    val errorInAuthenticationService: StateFlow<String> = _errorInAuthenticationServic.asStateFlow()

    fun loginRequest(
        signinRequest: SigninRequest
    ) {
        coroutineScope.launch {
            _successfulAuthenticationActivity.value = false
            _errorInAuthenticationServic.value = ""
            accountService.signIn(signinRequest.email, signinRequest.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _successfulAuthenticationActivity.value = true
                    } else {
                        _errorInAuthenticationServic.value = it.exception.toString()
                    }
                }
        }
    }

    fun registerUser(signUpRequest: SignUpRequest) {
        coroutineScope.launch {
            _successfulAuthenticationActivity.value = false
            _errorInAuthenticationServic.value = ""
            accountService.signUp(signUpRequest.email!!, signUpRequest.password!!)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _successfulAuthenticationActivity.value = true
                    } else {
                        _errorInAuthenticationServic.value = it.exception.toString()
                    }
                }
        }
    }


}