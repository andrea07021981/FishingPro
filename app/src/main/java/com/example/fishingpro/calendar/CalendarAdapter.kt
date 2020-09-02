package com.example.fishingpro.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.example.fishingpro.databinding.CalendarItemBinding

class CalendarAdapter(
    private val onClickListener: OnCalendarClickListener
) : ListAdapter<LocalDailyCatch, CalendarAdapter.CalendarViewHolder>(
    DiffCallback
){

    class CalendarViewHolder(
        val binding: CalendarItemBinding
    ) :RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnCalendarClickListener, item: LocalDailyCatch) {
            with(binding) {
                localDailyCatch = item
                calendarCallBack = clickListener
                executePendingBindings()
            }
        }

        companion object {
            //It creates a static version for java
            @JvmStatic val from = {parent: ViewGroup ->
                val inflater = LayoutInflater.from(parent.context)
                val binding = CalendarItemBinding.inflate(inflater)
                CalendarViewHolder(binding) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        return holder.bind(onClickListener, getItem(position))
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<LocalDailyCatch>() {
        override fun areItemsTheSame(oldItem: LocalDailyCatch, newItem: LocalDailyCatch): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: LocalDailyCatch, newItem: LocalDailyCatch): Boolean {
            return oldItem.date == newItem.date
        }
    }

    class OnCalendarClickListener(
        val clickListener: (catch: LocalDailyCatch) -> Unit
    ) {
        fun onClick(catch: LocalDailyCatch) = clickListener(catch)
    }
}
