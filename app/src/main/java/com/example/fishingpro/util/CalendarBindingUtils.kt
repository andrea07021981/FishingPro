package com.example.fishingpro.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingpro.calendar.CalendarAdapter
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalCatch

@BindingAdapter("dataList")
fun setCatchesDataList(recyclerView: RecyclerView, result: Result<List<LocalCatch>>?) {
    result.let {
        if (result is Result.Success) {
            val adapter = recyclerView.adapter as CalendarAdapter
            adapter.submitList(result.data)
        }
    }
}