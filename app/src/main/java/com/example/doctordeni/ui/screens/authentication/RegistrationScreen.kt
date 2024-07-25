package com.example.doctordeni.ui.screens.authentication

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.example.doctordeni.MainActivity
import com.example.doctordeni.models.requests.SignUpRequest
import com.example.doctordeni.navigation.BottomNavigation
import com.example.doctordeni.ui.components.BottomComponent
import com.example.doctordeni.ui.components.HeadingTextComponent
import com.example.doctordeni.ui.components.MyTextFieldComponent
import com.example.doctordeni.ui.components.NormalTextComponent
import com.example.doctordeni.ui.components.PasswordTextFieldComponent
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegistrationScreen(
    navController: NavHostController? = null
) {

    val authViewModel: AuthViewModel = koinViewModel()
    val successState = authViewModel.succesfulAuthenticationActivity.collectAsState()
    val failureState = authViewModel.errorInAuthenticationService.collectAsState()
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    if(successState.value){
        Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    if(failureState.value != ""){
        Toast.makeText(context, failureState.value, Toast.LENGTH_LONG).show()
    }

//    auth.addAuthStateListener { firebaseAuth ->
//        // Get the current user
//        val user = firebaseAuth.currentUser
//
//        // Check if the user is signed in
//        if (user != null) {
//            navController?.navigate(BottomNavigation.Home.route)
//            // User is signed in
//            // Do something here, such as navigate to the main activity
//        } else {
//            navController?.navigate("login")
//            // User is not signed in
//            // Do something here, such as navigate to the login activity
//        }
//    }

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
            NormalTextComponent(value = "Hello there,")
            HeadingTextComponent(value = "Create an Account")
            Spacer(modifier = Modifier.height(25.dp))

            Column {

                Spacer(modifier = Modifier.height(10.dp))
                MyTextFieldComponent(
                    labelValue = "Email", icon = Icons.Outlined.Email, authViewModel.regEmail
                )
                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextFieldComponent(
                    labelValue = "Password", icon = Icons.Outlined.Lock, authViewModel.password
                )
                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextFieldComponent(
                    labelValue = "Password Confirm",
                    icon = Icons.Outlined.Lock,
                    authViewModel.password1
                )
//                CheckboxComponent()
                BottomComponent(
                    textQuery = "Already have an account? ",
                    textClickable = "Login",
                    action = "Register",
                    navController!!,
                ) {
                    if (!authViewModel.password.value.equals(authViewModel.password1.value)) {
                        Toast.makeText(context, "Password is wrong", Toast.LENGTH_SHORT).show()
                    }

                    val signupRequest = SignUpRequest(
                        email = authViewModel.regEmail.value,
                        password = authViewModel.password.value,
                    )

                    authViewModel.registerUser(signupRequest)

                }
            }
        }
    }
}

@Composable
@Preview(name = "usiku", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "mchana", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
fun RegistrationScreenPreview(
    navHostController: NavHostController? = null
) {
    DoctorDeniTheme {
        RegistrationScreen()
    }
}