package com.example.doctordeni

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doctordeni.services.AccountService
import com.example.doctordeni.ui.screens.authentication.LoginScreen
import com.example.doctordeni.ui.screens.authentication.RegistrationScreen
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class AuthActivity : ComponentActivity() {

    val authService: AccountService by inject()
    val sessionManager: SessionManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()

// Add an AuthStateListener to the FirebaseAuth object
        auth.addAuthStateListener { firebaseAuth ->
            // Get the current user
            val user = firebaseAuth.currentUser

            // Check if the user is signed in
            if (user != null) {
                sessionManager.saveUserEmail(user.email ?: "email")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        setContent {
            DoctorDeniTheme(
                darkTheme = sessionManager.getAppTheme()
            ) {
                val navController = rememberNavController()
                if (authService.hasUser()) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController, startDestination = "Login"
                    ) {
                        composable(route = "Login") {
                            LoginScreen(navController)
                        }
                        composable(route = "Registration") {
                            RegistrationScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    DoctorDeniTheme {

    }
}