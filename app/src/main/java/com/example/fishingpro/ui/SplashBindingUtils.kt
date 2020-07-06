package com.example.fishingpro.ui

import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.hanks.htextview.rainbow.RainbowTextView


@BindingAdapter("animateText")
fun RainbowTextView.animate(@StringRes resource: Int) {
    animateText(resources.getString(resource))
}
