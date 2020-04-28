package com.daikaz.firebase.domain.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import kotlinx.coroutines.flow.Flow

interface FirebaseRemoteConfigStorage {
    suspend fun fetchAndActivate()
    fun fetch(): Flow<Boolean>
    fun activate(): Flow<Boolean>
    fun fetch(minimumFetchIntervalInSeconds: Long): Flow<Boolean>
    fun reset(): Flow<Boolean>
    fun setConfigSettingsAsync(settings: FirebaseRemoteConfigSettings)
    fun setDefaultsAsync(resId: Int)
    fun setDefaultsAsync(defaults: Map<String, Any>)

    fun getBoolean(key: String): Boolean
    fun getDouble(key: String): Double
    fun getKeysByPrefix(key: String): Set<String>
    fun getLong(key: String): Long
    fun getString(key: String): String
    fun getValue(key: String): FirebaseRemoteConfigValue
}
