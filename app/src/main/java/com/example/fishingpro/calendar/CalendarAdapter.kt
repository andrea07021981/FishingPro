package com.example.fishingpro.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.databinding.CalendarItemBinding

class CalendarAdapter(
    val onClickListener: OnCalendarClickListener
) : ListAdapter<LocalCatch, CalendarAdapter.CalendarViewHolder>(
    DiffCallback
){

    class CalendarViewHolder(
        val binding: CalendarItemBinding
    ) :RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnCalendarClickListener, item: LocalCatch) {
            with(binding) {
                localCatch = item
                calendarCallBack = clickListener
                executePendingBindings()
            }
        }

        companion object {
            val from = {parent: ViewGroup ->
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
    companion object DiffCallback : DiffUtil.ItemCallback<LocalCatch>() {
        override fun areItemsTheSame(oldItem: LocalCatch, newItem: LocalCatch): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: LocalCatch, newItem: LocalCatch): Boolean {
            return oldItem.CatchId == newItem.CatchId
        }
    }
    
    class OnCalendarClickListener(
        val clickListener: (catch: LocalCatch) -> Unit
    ) {
        fun onClick(catch: LocalCatch) = clickListener(catch)
    }
}
