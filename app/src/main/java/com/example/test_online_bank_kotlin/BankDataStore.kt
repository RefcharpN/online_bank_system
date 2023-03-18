package com.example.test_online_bank_kotlin

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BankDataStore(context: Context) {

    private var dataStore: DataStore<androidx.datastore.preferences.core.Preferences>

    init {
        dataStore = context.createDataStore(name = "settings")

    }


    public fun insert(key: String, value: String)
    {
        CoroutineScope(IO).launch {
            save(
                key,
                value
            )
        }
    }

    public suspend fun get_value(key: String): String
    {
            val value = read(key)
            return value?: "null"
    }


    private suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }

    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

}