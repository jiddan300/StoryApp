package com.example.storyapp.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val LOGIN_TOKEN  = stringPreferencesKey("token")

    fun getToken(): Flow<String>{
        return dataStore.data.map { Preferences ->
            Preferences[LOGIN_TOKEN] ?: ""
        }
    }

    suspend fun saveToken(token : String){
        dataStore.edit { Preferences ->
            Preferences[LOGIN_TOKEN]  = token
        }
    }

    suspend fun deleteToken(){
        dataStore.edit { Preferences ->
            Preferences[LOGIN_TOKEN]  = ""
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}