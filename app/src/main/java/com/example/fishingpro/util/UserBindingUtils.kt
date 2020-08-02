package com.example.fishingpro.util

import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fishingpro.R
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*


@BindingAdapter("weatherState")
fun ImageView.setWeatherState(weather: String?) {
    weather?.let {
        val url = "openweathermap.org/img/wn/"
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
}

@BindingAdapter("setDate")
fun CalendarView.setDate(date: Long) {
    setDate(date, true, true)
}

@BindingAdapter("setDateOfMonth")
fun TextView.setDateOfMonth(date: Long) {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    text = calendar[Calendar.DAY_OF_MONTH].toString()
}

@BindingAdapter("setDateOfWeek")
fun TextView.setDateOfWeek(date: Long) {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    text = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
}
