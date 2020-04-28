package com.daikaz.firebase.domain.repository

import com.google.firebase.firestore.*
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    operator fun set(ref: DocumentReference, value: Any, options: SetOptions?): Flow<Boolean>

    fun add(ref: CollectionReference, value: Any): Flow<DocumentReference?>

    fun update(ref: DocumentReference, value: Map<String?, Any?>): Flow<Boolean>

    fun <T> runTransaction(db: FirebaseFirestore, task: Transaction.Function<T>): Flow<T>

    fun commit(batch: WriteBatch): Flow<Boolean>

    fun delete(ref: DocumentReference): Flow<Boolean>

    fun deleteField(ref: DocumentReference, vararg fields: String): Flow<Boolean>

    operator fun get(ref: DocumentReference): Flow<DocumentSnapshot?>

    operator fun <T> get(ref: DocumentReference, clazz: Class<T>): Flow<T?>

    fun query(query: Query): Flow<QuerySnapshot>

    fun getAll(ref: CollectionReference): Flow<QuerySnapshot>

    fun documentReference(ref: DocumentReference): Flow<DocumentSnapshot>
}