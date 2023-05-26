package com.example.storyapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val detailStories = intent.getParcelableExtra<ListStoryItem>("key_user") as ListStoryItem
        binding.apply {
            tvDetailName.text = detailStories.name
            tvDetailDesc.text = detailStories.description
            Glide.with(applicationContext)
                .load(detailStories.photoUrl)
                .into(ivDetailImg)
        }
    }
}