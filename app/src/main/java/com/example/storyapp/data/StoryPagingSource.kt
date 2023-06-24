package com.example.storyapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.api.ApiService
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.helper.SettingPreferences
import kotlinx.coroutines.flow.first

class  StoryPagingSource(
    private val apiService: ApiService,
    private val pref: SettingPreferences)
    : PagingSource<Int, ListStoryItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = "Bearer ${pref.getToken().first()}"
            val responseData = apiService.getAllStories(token, page ,params.loadSize)
            val listStory = responseData.ListStoryItem
            LoadResult.Page(
                data = listStory,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            Log.e("getAllStories", pref.getToken().first())
            Log.e("getAllStories", "$exception")
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}