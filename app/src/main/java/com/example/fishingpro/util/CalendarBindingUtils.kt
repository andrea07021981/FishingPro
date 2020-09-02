package com.example.fishingpro.util

import android.widget.TextView
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingpro.calendar.CalendarAdapter
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.FishData
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.data.domain.LocalDailyCatch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("dataList")
fun setCatchesDataList(recyclerView: RecyclerView, result: Result<List<LocalDailyCatch>>?) {
    result.let {
        if (result is Result.Success) {
            val adapter = recyclerView.adapter as CalendarAdapter
            adapter.submitList(result.data)
        }
    }
}

@BindingAdapter("formatMonthYear")
fun TextView.formatYear(date: Date) {
    text = with(SimpleDateFormat("MMMM yyyy")) {
        format(date)
    }
}

@BindingAdapter("formatWeekDay")
fun TextView.formatWeek(date: Date) {
    text = with(SimpleDateFormat("EEEE")) {
        format(date)
    }
}

@BindingAdapter("formatDay")
fun TextView.formatDay(date: Date) {
    text = Calendar.getInstance().apply {
        time = date
    }.get(Calendar.DAY_OF_MONTH).toString()
}

@BindingAdapter("countCatches")
fun TextView.countCatches(localDailyCatch: List<FishData>) {
    text = localDailyCatch.count().toString()
}