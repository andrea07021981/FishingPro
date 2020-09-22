package com.example.fishingpro.util

import android.location.Location
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingpro.fish.FishAdapter
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.FishData
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.firebase.firestore.GeoPoint
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("dataList")
fun setCatchesDataList(recyclerView: RecyclerView, result: List<LocalDailyCatch>?) {
    result.let {
            val adapter = recyclerView.adapter as FishAdapter
            adapter.submitList(result?.sortedBy { it.date })
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

@BindingAdapter("formatCompleteDate")
fun CollapsingToolbarLayout.formatCompleteDate(date: Date?) {
    date?.let {
        title = with(SimpleDateFormat("dd-MM-yyyy")) {
            format(it)
        }
    }
}

@BindingAdapter("countCatches")
fun TextView.countCatches(localDailyCatch: List<FishData>) {
    text = localDailyCatch.count().toString()
}

fun GeoPoint.toLatLng(): LatLng {
    return this.run {
        LatLng(this.latitude, this.longitude)
    }
}

fun Location.toLatLng(): LatLng {
    return this.run {
        LatLng(this.latitude, this.longitude)
    }
}

fun Location?.toText():String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}
