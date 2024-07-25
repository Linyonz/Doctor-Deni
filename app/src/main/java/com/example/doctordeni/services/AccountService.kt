package com.example.doctordeni.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.Flow

interface AccountService {

   suspend fun signIn(email:String,password:String): Task<AuthResult>

   suspend fun signUp(email:String, password:String): Task<AuthResult>

   suspend fun signOut()

   suspend fun deleteAccount()

   fun hasUser():Boolean
}