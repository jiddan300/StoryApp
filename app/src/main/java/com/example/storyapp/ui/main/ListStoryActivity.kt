package com.example.storyapp.ui.main

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.api.AllStoriesResponse
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.databinding.ActivityListStoryBinding
import com.example.storyapp.helper.PrefViewModel
import com.example.storyapp.helper.PrefViewModelFactory
import com.example.storyapp.helper.SettingPreferences
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.authentication.LoginActivity
import com.example.storyapp.ui.authentication.dataStore
import com.example.storyapp.ui.upload.AddStoryActivity
import kotlin.system.exitProcess

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding

    private  lateinit var viewModel : ListStoryViewModel
    private lateinit var prefViewModel: PrefViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "List Story"

        val layoutManager = LinearLayoutManager(this)
        binding.rvListStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListStory.addItemDecoration(itemDecoration)

        binding.fabAddStory.setOnClickListener{
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
        val pref = SettingPreferences.getInstance(dataStore)
        prefViewModel = ViewModelProvider(this, PrefViewModelFactory(pref)).get(PrefViewModel::class.java)

        viewModel = obtainViewModel(this)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        prefViewModel.getToken().observe(this){
            if (it.isNotEmpty()){
                viewModel.getListStory(it)
            }
        }

        viewModel.allStory.observe(this){
            insertListStory(it)
        }
    }

    override fun onResume() {
        super.onResume()
        prefViewModel.getToken().observe(this){
            if (it.isNotEmpty()){
                viewModel.getListStory(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val pref = SettingPreferences.getInstance(dataStore)
        val prefViewModel = ViewModelProvider(this, PrefViewModelFactory(pref)).get(PrefViewModel::class.java)

        when (item.itemId) {
            R.id.menu_logout -> {
                prefViewModel.deleteToken()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return true
            }
            else -> return true
        }
    }

    private fun insertListStory(it: AllStoriesResponse) {
        val adapter = ListStoryAdapter(it.listStory as List<ListStoryItem>)
        binding.rvListStory.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbAllStory.visibility = View.VISIBLE
        } else {
            binding.pbAllStory.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): ListStoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(ListStoryViewModel::class.java)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Close?")
        builder.setMessage("Close App?")

        builder.setPositiveButton("Yes"){ dialog : DialogInterface, which: Int ->
            finish()
            exitProcess(0)
        }

        builder.setNegativeButton("No"){ dialog : DialogInterface, which: Int ->
            dialog.cancel()
        }
        builder.show()
    }
}