package com.example.fishingpro.fish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.example.fishingpro.databinding.FishItemBinding

class FishAdapter(
    private val onClickListener: OnFishClickListener
) : ListAdapter<LocalDailyCatch, FishAdapter.FishViewHolder>(
    DiffCallback
){

    class FishViewHolder(
        val binding: FishItemBinding
    ) :RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnFishClickListener, item: LocalDailyCatch) {
            with(binding) {
                localDailyCatch = item
                fishCallBack = clickListener
                executePendingBindings()
            }
        }

        companion object {
            //It creates a static version for java
            @JvmStatic val from = {parent: ViewGroup ->
                val inflater = LayoutInflater.from(parent.context)
                val binding = FishItemBinding.inflate(inflater)
                FishViewHolder(binding) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FishViewHolder {
        return FishViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: FishViewHolder, position: Int) {
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

    class OnFishClickListener(
        val clickListener: (catch: LocalDailyCatch) -> Unit
    ) {
        fun onClick(catch: LocalDailyCatch) = clickListener(catch)
    }
}
