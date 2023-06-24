package com.example.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.api.*
import com.example.storyapp.data.StoryRepo

class ListStoryViewModel(private val repo : StoryRepo) : ViewModel() {
    fun getStory() : LiveData<PagingData<ListStoryItem>> {
        return repo.getStory().cachedIn(viewModelScope) }
}