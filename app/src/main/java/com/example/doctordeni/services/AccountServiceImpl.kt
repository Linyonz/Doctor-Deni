package com.example.doctordeni.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await


class AccountServiceImpl(
    private val authFirebaseAuth: FirebaseAuth
):AccountService {
    override suspend fun signIn(email: String, password: String): Task<AuthResult> {
       return authFirebaseAuth.signInWithEmailAndPassword(email,password)

    }

    override suspend fun signUp(email: String, password: String): Task<AuthResult> {
       return authFirebaseAuth.createUserWithEmailAndPassword(email,password)
    }

    override suspend fun signOut() {
        authFirebaseAuth.signOut()
    }

    override suspend fun deleteAccount() {
        authFirebaseAuth.currentUser!!.delete().await()
    }

    override fun hasUser(): Boolean {
        return authFirebaseAuth.currentUser != null
    }
}