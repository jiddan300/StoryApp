package com.example.storyapp.ui.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.LoginResponse
import com.example.storyapp.api.RegisterUploadResponse
import retrofit2.Call
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val _loginDetail = MutableLiveData<LoginResponse>()
    val loginDetail: LiveData<LoginResponse> = _loginDetail

    private val _registDetail = MutableLiveData<RegisterUploadResponse>()
    val registDetail: LiveData<RegisterUploadResponse> = _registDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email:String, password:String){
        _isLoading.value = true
        val postLogin = ApiConfig.getApiService().postLogin(email, password)
        postLogin.enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    _loginDetail.value = response.body()
                } else{
                    Log.e("Login Failed", "Login onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("enqueue Failed", "Login onFailure: ${t.message}")
            }

        })
    }

    fun register(name : String, email:String, password:String){
        _isLoading.value = true
        val postLogin = ApiConfig.getApiService().postRegister(name, email, password)
        postLogin.enqueue(object : retrofit2.Callback<RegisterUploadResponse>{
            override fun onResponse(call: Call<RegisterUploadResponse>, response: Response<RegisterUploadResponse>) {
                if (response.isSuccessful){
                    _registDetail.value = response.body()
                } else{
                    Log.e("Register Failed", "Register onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<RegisterUploadResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("enqueue Failed", "Register onFailure: ${t.message}")
            }

        })
    }
}