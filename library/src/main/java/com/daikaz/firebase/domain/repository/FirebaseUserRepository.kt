package com.daikaz.firebase.domain.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow

interface FirebaseUserRepository {

    fun delete(): Flow<Boolean>

    fun getToken(): Flow<String?>

    fun linkWithCredential(credential: AuthCredential): Flow<FirebaseUser?>

    fun reauthenticate(credential: AuthCredential): Flow<Boolean>

    fun reload(): Flow<Boolean>

    fun sendVerifyEmail(): Flow<Boolean>

    fun unlink(provider: String): Flow<Boolean>

    fun updateEmail(email: String): Flow<Boolean>

    fun updatePassword(pw: String): Flow<Boolean>

    fun updateProfile(request: UserProfileChangeRequest): Flow<Boolean>
}