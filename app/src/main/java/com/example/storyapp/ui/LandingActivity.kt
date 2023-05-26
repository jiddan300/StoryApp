package com.example.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.helper.PrefViewModel
import com.example.storyapp.helper.PrefViewModelFactory
import com.example.storyapp.helper.SettingPreferences
import com.example.storyapp.ui.authentication.LoginActivity
import com.example.storyapp.ui.authentication.dataStore
import com.example.storyapp.ui.main.ListStoryActivity

class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)


        val pref = SettingPreferences.getInstance(dataStore)
        val prefViewModel = ViewModelProvider(this, PrefViewModelFactory(pref)).get(PrefViewModel::class.java)


        prefViewModel.getToken().observe(this){
            if (it.isNotEmpty()){
                startActivity(Intent(this, ListStoryActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}