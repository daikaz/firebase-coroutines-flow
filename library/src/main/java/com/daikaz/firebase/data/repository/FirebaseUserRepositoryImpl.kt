package com.daikaz.firebase.data.repository

import com.daikaz.firebase.await
import com.daikaz.firebase.domain.repository.FirebaseUserRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.flow

class FirebaseUserRepositoryImpl(private val firebaseUser: FirebaseUser?) : FirebaseUserRepository {

    companion object {
        private const val EXPIRED_GAP = 300000L
    }

    override fun delete() = flow {
        firebaseUser?.delete()?.await()
        emit(true)
    }

    override fun getToken() = flow {
        var task = firebaseUser?.getIdToken(false)?.await()
        var expireTime: Long = task?.expirationTimestamp ?: 0L

        if (expireTime < 100_0000_000_000L) {
            expireTime *= 1000L
        }

        val isAlive = expireTime - System.currentTimeMillis() > EXPIRED_GAP

        if (!isAlive) {
            task = firebaseUser?.getIdToken(true)?.await()
        }

        if (task?.token?.isNotEmpty() == true) {
            emit(task.token)
            return@flow
        }
        throw IllegalStateException("Token does not exists.")
    }

    override fun linkWithCredential(credential: AuthCredential) = flow {
        val task = firebaseUser?.linkWithCredential(credential)?.await()
        emit(task?.user)
    }

    override fun reauthenticate(credential: AuthCredential) = flow {
        firebaseUser?.reauthenticate(credential)?.await()
        emit(true)
    }

    override fun reload() = flow {
        firebaseUser?.reload()?.await()
        emit(true)
    }

    override fun sendVerifyEmail() = flow {
        firebaseUser?.sendEmailVerification()?.await()
        emit(true)
    }

    override fun unlink(provider: String) = flow {
        try {
            firebaseUser?.unlink(provider)?.await()
            emit(true)
        } catch (e: Exception) {
            if (e.message?.contains("User was not linked to an account with the given provider") == true) {
                emit(true)
                return@flow
            }
            throw e
        }
    }

    override fun updateEmail(email: String) = flow {
        firebaseUser?.updateEmail(email)?.await()
        emit(true)
    }

    override fun updatePassword(pw: String) = flow {
        firebaseUser?.updatePassword(pw)?.await()
        emit(true)
    }

    override fun updateProfile(request: UserProfileChangeRequest) = flow {
        firebaseUser?.updateProfile(request)?.await()
        emit(true)
    }
}