package com.example.storyapp.ui.main

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.adapter.LoadStoryAdapter
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.databinding.ActivityListStoryBinding
import com.example.storyapp.helper.PrefViewModel
import com.example.storyapp.helper.PrefViewModelFactory
import com.example.storyapp.helper.SettingPreferences
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.authentication.LoginActivity
import com.example.storyapp.ui.authentication.dataStore
import com.example.storyapp.ui.storymap.MapsActivity
import com.example.storyapp.ui.upload.AddStoryActivity
import kotlin.system.exitProcess

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding

    private  lateinit var viewModel : ListStoryViewModel
    private lateinit var prefViewModel: PrefViewModel
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "List Story"

        binding.fabAddStory.setOnClickListener{
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        val pref = SettingPreferences.getInstance(dataStore)
        prefViewModel = ViewModelProvider(this, PrefViewModelFactory(pref)).get(PrefViewModel::class.java)

        val factory = ViewModelFactory.getInstance(this)

        viewModel = ViewModelProvider(this, factory).get(ListStoryViewModel::class.java)

        adapter = StoryAdapter()
        binding.rvListStory.layoutManager = LinearLayoutManager(this)
        binding.rvListStory.setHasFixedSize(true)

        prefViewModel.getToken().observe(this){
            if (it.isNotEmpty()){
                perAdapter()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        prefViewModel.getToken().observe(this){
            if (it.isNotEmpty()){
                perAdapter()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_map ->{
                startActivity(Intent(this, MapsActivity::class.java))
                return true
            }
            R.id.menu_logout -> {
                prefViewModel.deleteToken()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return true
            }
            else -> return true
        }
    }

    private fun perAdapter() {
        binding.rvListStory.adapter = adapter.withLoadStateFooter(
            footer = LoadStoryAdapter { adapter.retry() }
        )
        viewModel.getStory().observe(this) {
            adapter.submitData(lifecycle, it)
        }
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