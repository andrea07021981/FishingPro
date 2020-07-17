package com.example.fishingpro.util

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fishingpro.R

@BindingAdapter("weatherState")
fun ImageView.setWeatherState(weather: String) {
    val url = "openweathermap.org/img/wn"
        .plus(weather)
        .plus("@2x.png")
        .toUri()
        .buildUpon()
        .scheme("https")
        .build()
    Glide.with(context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_baseline_sync_24)
                .error(R.drawable.ic_baseline_broken_image_24))
        .into(this)
}