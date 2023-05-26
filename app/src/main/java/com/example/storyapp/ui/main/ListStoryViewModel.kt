package com.example.storyapp.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.AllStoriesResponse
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.LoginResponse
import com.example.storyapp.api.RegisterUploadResponse
import retrofit2.Call
import retrofit2.Response

class ListStoryViewModel(application: Application) : ViewModel()  {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _allStory = MutableLiveData<AllStoriesResponse>()
    val allStory: LiveData<AllStoriesResponse> = _allStory

    fun getListStory(token: String){
        _isLoading.value = true
        val postLogin = ApiConfig.getApiService().getAllStories("Bearer $token")
        postLogin.enqueue(object : retrofit2.Callback<AllStoriesResponse>{
            override fun onResponse(call: Call<AllStoriesResponse>, response: Response<AllStoriesResponse>) {
                if (response.isSuccessful){
                    _allStory.value = response.body()
                } else{
                    Log.e("Login Failed", "Login onFailure: ${response.message()}, token = $token")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("enqueue Failed", "Login onFailure: ${t.message}")
            }

        })
    }
}