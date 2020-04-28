package com.daikaz.firebase.domain.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {

    fun authStateChanges(): Flow<FirebaseAuth>

    fun createUserWithEmailAndPassword(email: String, password: String): Flow<AuthResult>

    fun fetchProvidersForEmail(email: String): Flow<List<String>>

    fun getCurrentUser(): Flow<FirebaseUser?>

    fun sendPasswordResetEmail(email: String): Flow<Boolean>

    fun signInAnonymous(): Flow<FirebaseUser?>

    fun signInWithCredential(credential: AuthCredential): Flow<FirebaseUser?>

    fun signInWithCustomToken(token: String): Flow<FirebaseUser?>

    fun signInWithEmailAndPassword(email: String, password: String): Flow<FirebaseUser?>

    fun signUpWithEmailAndPassword(email: String, password: String): Flow<FirebaseUser?>

    fun signOut(): Flow<Boolean>
}