package com.example.fishingpro.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.weather.WeatherAdapter


@BindingAdapter("dataList")
fun setDataList(recyclerView: RecyclerView, data: List<LocalWeatherDomain>?) {
    val adapter = recyclerView.adapter as WeatherAdapter
    adapter.submitList(data)
}