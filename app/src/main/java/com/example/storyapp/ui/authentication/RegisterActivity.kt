package com.example.storyapp.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.helper.ViewModelFactory
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private var correctName = false
    private var correctEmail = false
    private var correctPassword = false

    private val Password_Pattern = Pattern.compile("^" +
            //"(?=.*[@#$%^&+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{8,}" +                // at least 8 characters
            "$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Register"

        val viewModel = obtainViewModel(this)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.registDetail.observe(this){
            response()
        }

        binding.etNameRegist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()){
                    correctName = true
                    setBtnEnabled(binding)
                }else {
                    correctName = false
                    setBtnEnabled(binding)
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }

        })

        binding.etEmailRegist.addTextChangedListener(object : TextWatcher {
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

        binding.etPasswordRegist.addTextChangedListener(object : TextWatcher {
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

        binding.btnRegist.setOnClickListener {
            val name = binding.etNameRegist.text.toString()
            val email = binding.etEmailRegist.text.toString()
            val password = binding.etPasswordRegist.text.toString()

            viewModel.register(name, email, password)
        }

    }

    private fun setBtnEnabled(binding: ActivityRegisterBinding) {
        binding.btnRegist.isEnabled = correctName && correctEmail && correctPassword
    }

    private fun checkError(binding: ActivityRegisterBinding): Boolean {
        return binding.etNameRegist.error == null && binding.etEmailRegist.error == null &&binding.etPasswordRegist.error == null

    }

    private fun response() {
        finish()
    }


    private fun obtainViewModel(activity: AppCompatActivity): AuthViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(AuthViewModel::class.java)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbRegister.visibility = View.VISIBLE
        } else {
            binding.pbRegister.visibility = View.GONE
        }
    }

}