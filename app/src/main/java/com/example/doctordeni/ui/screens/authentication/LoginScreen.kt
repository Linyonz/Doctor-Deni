package com.example.doctordeni.ui.screens.authentication

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.doctordeni.MainActivity
import com.example.doctordeni.models.requests.SigninRequest
import com.example.doctordeni.ui.components.BottomComponent
import com.example.doctordeni.ui.components.HeadingTextComponent
import com.example.doctordeni.ui.components.MyTextFieldComponent
import com.example.doctordeni.ui.components.NormalTextComponent
import com.example.doctordeni.ui.components.PasswordTextFieldComponent
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.viewmodels.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavHostController? = null
) {

    val authViewModel: AuthViewModel = koinViewModel()
    val successState = authViewModel.succesfulAuthenticationActivity.collectAsState()
    val failureState = authViewModel.errorInAuthenticationService.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    if (successState.value) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    if (failureState.value != "") {
        Toast.makeText(context, failureState.value, Toast.LENGTH_LONG).show()
    }
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Column {
                NormalTextComponent(value = "Hey, there")
                HeadingTextComponent(value = "Welcome Back")
            }
            Spacer(modifier = Modifier.height(25.dp))
            Column {
                MyTextFieldComponent(
                    labelValue = "Email", icon = Icons.Outlined.Email, authViewModel.signinEmail
                )
                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextFieldComponent(
                    labelValue = "Password",
                    icon = Icons.Outlined.Lock,
                    authViewModel.signinPassword
                )
            }
            BottomComponent(
                textQuery = "Don't have an account? ",
                textClickable = "Register",
                action = "Login",
                navController!!
            ) {

                if (authViewModel.signinEmail.value == null) {
                    Toast.makeText(context, "email is empty", Toast.LENGTH_SHORT).show()
                }

                if (authViewModel.signinPassword.value == null) {
                    Toast.makeText(context, "password is empty", Toast.LENGTH_SHORT).show()
                }
                val signingRequest = SigninRequest(
                    authViewModel.signinEmail.value, authViewModel.signinPassword.value
                )
                authViewModel.loginRequest(signingRequest)
                Log.d("SigningInRequest", signingRequest.toString())
            }

        }
    }
}

@Composable
@Preview(name = "mchana", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "usiku", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun LoginScreenPreview(navHostController: NavHostController? = null) {
    DoctorDeniTheme {
        LoginScreen()
    }
}