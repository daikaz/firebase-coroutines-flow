package com.daikaz.firebase.data.repository

import com.daikaz.firebase.await
import com.daikaz.firebase.domain.repository.FirestoreRepository
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.util.*

class FirestoreRepositoryImpl : FirestoreRepository {

    override operator fun set(ref: DocumentReference, value: Any, options: SetOptions?) = flow {
        if (options == null) {
            ref.set(value)
        } else {
            ref.set(value, options)
        }.await()
        emit(true)
    }

    override fun add(ref: CollectionReference, value: Any) = flow {
        val task = ref.add(value)
        emit(task.result)
    }

    override fun update(ref: DocumentReference, value: Map<String?, Any?>) = flow {
        ref.update(value).await()
        emit(true)
    }

    override fun <T> runTransaction(db: FirebaseFirestore, task: Transaction.Function<T>) = flow {
        val t = db.runTransaction(task).await()
        emit(t)
    }

    override fun commit(batch: WriteBatch) = flow {
        batch.commit().await()
        emit(true)
    }

    override fun delete(ref: DocumentReference) = flow {
        ref.delete().await()
        emit(true)
    }

    override fun deleteField(ref: DocumentReference, vararg fields: String) = flow {
        val deletes: MutableMap<String, Any> = HashMap()
        for (f in fields) {
            deletes[f] = FieldValue.delete()
        }
        ref.update(deletes).await()
        emit(true)
    }

    override operator fun get(ref: DocumentReference) = flow {
        val snapshot = ref.get().await()
        if (null == snapshot || !snapshot.exists()) {
            emit(null)
            return@flow
        }
        if (snapshot.exists()) {
            emit(snapshot)
            return@flow
        }
        throw NoSuchElementException("This document doesn't exist.")
    }

    override operator fun <T> get(ref: DocumentReference, clazz: Class<T>) = flow {
        val snapshot = ref.get().await()
        if (null != snapshot && snapshot.exists()) {
            emit(snapshot.toObject(clazz))
        } else {
            throw NoSuchElementException("This document doesn't exist.")
        }
    }

    override fun query(query: Query) = flow {
        val querySnapshot = query.get().await()
        emit(querySnapshot)
    }

    override fun getAll(ref: CollectionReference) = flow {
        val documentSnapshots = ref.get().await()
        emit(documentSnapshots)
    }

    override fun documentReference(ref: DocumentReference): Flow<DocumentSnapshot> {
        return callbackFlow {
            val register = ref.addSnapshotListener { querySnapshot, _ ->
                if (null != querySnapshot) {
                    this@callbackFlow.sendBlocking(querySnapshot)
                }
            }
            awaitClose {
                register.remove()
            }
        }
    }
}