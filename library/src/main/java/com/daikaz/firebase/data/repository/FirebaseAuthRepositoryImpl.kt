package com.daikaz.firebase.data.repository

import com.daikaz.firebase.await
import com.daikaz.firebase.domain.repository.FirebaseAuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.util.*

class FirebaseAuthRepositoryImpl(private val firebaseAuth: FirebaseAuth) : FirebaseAuthRepository {
    override fun authStateChanges() = callbackFlow<FirebaseAuth> {
        val listener = FirebaseAuth.AuthStateListener {
            this@callbackFlow.sendBlocking(it)
        }
        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    override fun createUserWithEmailAndPassword(email: String, password: String) = flow {
        emit(firebaseAuth.signInWithEmailAndPassword(email, password).await())
    }

    override fun fetchProvidersForEmail(email: String) = flow {
        val task = firebaseAuth.fetchSignInMethodsForEmail(email).await()
        val providers: List<String> = task.signInMethods ?: ArrayList()
        emit(providers)
    }

    override fun getCurrentUser() = flow {
        emit(firebaseAuth.currentUser)
    }

    override fun sendPasswordResetEmail(email: String) = flow {
        firebaseAuth.sendPasswordResetEmail(email).await()
        emit(true)
    }

    override fun signInAnonymous() = flow {
        val task = firebaseAuth.signInAnonymously().await()
        emit(task.user)
    }

    override fun signInWithCredential(credential: AuthCredential) = flow {
        val task = firebaseAuth.signInWithCredential(credential).await()
        emit(task.user)
    }

    override fun signInWithCustomToken(token: String) = flow {
        val task = firebaseAuth.signInWithCustomToken(token).await()
        emit(task.user)
    }

    override fun signInWithEmailAndPassword(email: String, password: String) = flow {
        val task = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        emit(task.user)
    }

    override fun signUpWithEmailAndPassword(email: String, password: String) = flow {
        val task = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        emit(task.user)
    }

    override fun signOut() = flow {
        firebaseAuth.signOut()
        emit(true)
    }

}