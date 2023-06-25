package com.example.storyapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val detailStories = intent.getParcelableExtra<ListStoryItem>("extra_name") as ListStoryItem
        binding.apply {
            tvDetailName.text = detailStories.name
            tvDetailDesc.text = detailStories.description
            Glide.with(applicationContext)
                .load(detailStories.photoUrl)
                .into(ivDetailImg)
        }
    }
}
