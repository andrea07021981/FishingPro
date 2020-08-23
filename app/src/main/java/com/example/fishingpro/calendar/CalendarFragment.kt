package com.example.fishingpro.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.CalendarViewBindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fishingpro.databinding.FragmentCalendarBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CalendarFragment() : Fragment() {

    private val calendarViewModel: CalendarViewModel by viewModels()
    private lateinit var dataBinding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentCalendarBinding.inflate(inflater).also {
            it.calendarViewModel = calendarViewModel
            it.lifecycleOwner = this
        }
        dataBinding.calendarToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return dataBinding.root
    }
}