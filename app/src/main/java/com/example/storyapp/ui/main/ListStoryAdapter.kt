package com.example.storyapp.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.api.ListStoryItem
import androidx.core.util.Pair

class ListStoryAdapter(private val listStory: List<ListStoryItem>) : RecyclerView.Adapter<ListStoryAdapter.ViewHolder>()   {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_list_story, viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUsername.text = listStory[position].name
        Glide.with(holder.tvUsername)
            .load(listStory[position].photoUrl)
            .into(holder.ivImage)

        holder.itemView.setOnClickListener{
            val intentDetail = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intentDetail.putExtra("key_user", listStory[holder.adapterPosition])

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.ivImage, "profile"),
                    Pair(holder.tvUsername, "name")
                )
            holder.itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
        }

    }

    override fun getItemCount() = listStory.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById(R.id.tv_item_name)
        val ivImage : ImageView = itemView.findViewById(R.id.iv_item_img)
    }
}