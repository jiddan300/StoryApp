package com.example.storyapp

import com.example.storyapp.api.ListStoryItem

object DummyData {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val item = arrayListOf<ListStoryItem>()
        for (i in 0 until 10) {
            val story = ListStoryItem(
                "siapa aja",
                "cek lokasi",
                "https://images.pexels.com/photos/3861972/pexels-photo-3861972.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "2022-11-02",
                106.64356,
                -6.1335033,
                "user-XNkh2yhu1ETa8Wvt",
            )
            item.add(story)
        }
        return item
    }
}