package com.example.fishingpro.ui

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("hasError")
fun TextInputLayout.hasError(error: Boolean) {
    when (error) {
        true -> setError("Mandatory field")
        else -> setError(null)
    }
}

