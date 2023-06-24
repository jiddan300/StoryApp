package com.example.storyapp.ui.authentication

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.api.LoginResponse
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.helper.PrefViewModel
import com.example.storyapp.helper.PrefViewModelFactory
import com.example.storyapp.helper.SettingPreferences
import com.example.storyapp.ui.main.ListStoryActivity
import java.util.regex.Pattern
import kotlin.system.exitProcess

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var correctEmail = false
    private var correctPassword = false

    private val Password_Pattern = Pattern.compile("^" +
            //"(?=.*[@#$%^&+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{8,}" +                // at least 8 characters
            "$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title  = "Login"

        binding.tvRegisterNow.setOnClickListener{
            val moveIntent = Intent(this, RegisterActivity::class.java)
            val moveAnimation = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            startActivity(moveIntent, moveAnimation)
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val prefViewModel = ViewModelProvider(this, PrefViewModelFactory(pref)).get(PrefViewModel::class.java)
        val viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.loginDetail.observe(this){
            prefViewModel.saveToken(it.loginResult?.token.toString())
            response(it, prefViewModel)
        }

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                    correctEmail = true
                    setBtnEnabled(binding)
                }else {
                    correctEmail = false
                    setBtnEnabled(binding)
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }

        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty() && Password_Pattern.matcher(s).matches()){
                    correctPassword = true
                    setBtnEnabled(binding)
                }else {
                    correctPassword = false
                    setBtnEnabled(binding)
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }

        })

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }
    }

    private fun setBtnEnabled(binding: ActivityLoginBinding){
        binding.btnLogin.isEnabled = correctEmail && correctPassword
    }

    private fun response(it: LoginResponse, prefViewModel: PrefViewModel) {
        prefViewModel.saveToken(it.loginResult?.token.toString())
        startActivity(Intent(this, ListStoryActivity::class.java))
        finish()
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLogin.visibility = View.VISIBLE
        } else {
            binding.pbLogin.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Close?")
        builder.setMessage("Close App?")

        builder.setPositiveButton("Yes"){dialog : DialogInterface, which: Int ->
            finish()
            exitProcess(0)
        }

        builder.setNegativeButton("No"){dialog : DialogInterface, which: Int ->
            dialog.cancel()
        }
        builder.show()
    }
}