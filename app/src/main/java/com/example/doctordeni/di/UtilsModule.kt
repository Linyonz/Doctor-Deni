package com.example.doctordeni.di

import android.content.Context
import com.example.doctordeni.services.AccountService
import com.example.doctordeni.services.AccountServiceImpl
import com.example.doctordeni.utils.SessionManager
import com.github.javafaker.Faker
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.koin.dsl.module

val utilsModule = module {

    single{ provideFirestore() }
    single{ provideAuth() }
    single{ provideAccountService(get()) }
    single{ provideDatabaseReference() }
    single { provideSession(get()) }
    single { provideFaker() }
}

fun provideFirestore(): FirebaseFirestore {
    return Firebase.firestore
}

fun provideAuth(): FirebaseAuth {
    return Firebase.auth
}

fun provideAccountService(auth: FirebaseAuth):AccountService{
    return AccountServiceImpl(auth)
}

fun provideDatabaseReference(): DatabaseReference {
    Firebase.database.setPersistenceEnabled(true)
    val firebaseDB = Firebase.database.reference
    firebaseDB.keepSynced(true)
    return firebaseDB
}

fun provideSession(context:Context): SessionManager {
    return SessionManager(context)
}

fun provideFaker(): Faker {
    return Faker()
}