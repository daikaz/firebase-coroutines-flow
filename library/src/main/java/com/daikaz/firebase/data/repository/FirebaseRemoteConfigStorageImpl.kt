package com.daikaz.firebase.data.repository

import com.daikaz.firebase.await
import com.daikaz.firebase.domain.repository.FirebaseRemoteConfigStorage
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.flow.flow

open class FirebaseRemoteConfigStorageImpl(
    private val remoteConfig: FirebaseRemoteConfig
) : FirebaseRemoteConfigStorage {

    override suspend fun fetchAndActivate() {
        remoteConfig.fetchAndActivate().await()
    }

    override fun fetch() = flow {
        remoteConfig.fetch().await()
        emit(true)
    }

    override fun fetch(minimumFetchIntervalInSeconds: Long) = flow {
        remoteConfig.fetch(minimumFetchIntervalInSeconds).await()
        emit(true)
    }

    override fun activate() = flow {
        emit(remoteConfig.activate().await())
    }

    override fun reset() = flow {
        remoteConfig.reset().await()
        emit(true)
    }

    override fun setConfigSettingsAsync(settings: FirebaseRemoteConfigSettings) {
        remoteConfig.setConfigSettingsAsync(settings)
    }

    override fun setDefaultsAsync(resId: Int) {
        remoteConfig.setDefaultsAsync(resId)
    }

    override fun setDefaultsAsync(defaults: Map<String, Any>) {
        remoteConfig.setDefaultsAsync(defaults)
    }

    override fun getBoolean(key: String) = remoteConfig.getBoolean(key)

    override fun getDouble(key: String) = remoteConfig.getDouble(key)

    override fun getKeysByPrefix(key: String): MutableSet<String> = remoteConfig.getKeysByPrefix(key)

    override fun getLong(key: String) = remoteConfig.getLong(key)

    override fun getString(key: String) = remoteConfig.getString(key)

    override fun getValue(key: String) = remoteConfig.getValue(key)
}
