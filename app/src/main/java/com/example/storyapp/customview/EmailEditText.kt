package com.example.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class EmailEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas : Canvas){
        super.onDraw(canvas)

        hint = "Email Address"
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    }


    private fun init() {
        addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()){
                    error = "Field Can Not Be Empty"
                }
                else if (s.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                    error = null
                } else {
                    error = "Invalid Email Address"
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}