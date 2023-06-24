package com.example.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.data.StoryRepo
import com.example.storyapp.helper.SettingPreferences

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
object Injection {
    fun provideRepository(context: Context): StoryRepo {
        val preferences = SettingPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepo(preferences, apiService)
    }
}