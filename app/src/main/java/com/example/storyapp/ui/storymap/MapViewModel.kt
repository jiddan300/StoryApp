package com.example.storyapp.ui.storymap

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.AllStoriesResponse
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.ListStoryItem
import retrofit2.Call
import retrofit2.Response

class MapViewModel : ViewModel() {

    private val _listStoryMap  = MutableLiveData<List<ListStoryItem>>()
    val listStoryMap : LiveData<List<ListStoryItem>> = _listStoryMap


    fun getListMap(token:String){
        val getLocation = ApiConfig.getApiService().getStoriesLocation("Bearer $token", 1)
        getLocation.enqueue(object : retrofit2.Callback<AllStoriesResponse>{
            override fun onResponse(call: Call<AllStoriesResponse>, response: Response<AllStoriesResponse>) {
                if (response.isSuccessful){
                    _listStoryMap.value = response.body()?.ListStoryItem
                } else{
                    Log.e("Get Location", "onResponse Failure: ${token}")
                    Log.e("Get Location", "onResponse Failure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                Log.e("Get Location", "enqueue onFailure: ${t.message}")
            }

        })
    }
}