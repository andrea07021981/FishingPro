package com.example.fishingpro.weather

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.databinding.WeatherItemBinding
import java.util.*

class WeatherAdapter(
    private val onClickListener: OnWeatherClickListener
) : ListAdapter<LocalWeatherDomain, WeatherAdapter.WeatherViewHolder>(
    DiffCallback
){

    class WeatherViewHolder private constructor(
        val binding: WeatherItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OnWeatherClickListener, item: LocalWeatherDomain) {
            with(binding) {
                localWeatherDomain = item
                weatherCallback = clickListener
                executePendingBindings()
            }
        }

        companion object {
            val from = {parent: ViewGroup ->
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WeatherItemBinding.inflate(layoutInflater, parent, false)
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                binding.weatherCardView.setBackgroundColor(color)
                WeatherViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        return holder.bind(onClickListener, getItem(position))
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<LocalWeatherDomain>() {
        override fun areItemsTheSame(oldItem: LocalWeatherDomain, newItem: LocalWeatherDomain): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: LocalWeatherDomain, newItem: LocalWeatherDomain): Boolean {
            return oldItem.wId == newItem.wId
        }
    }

    class OnWeatherClickListener(
        val clickListener: (weather: LocalWeatherDomain) -> Unit
    ) {
        fun onClick(weather: LocalWeatherDomain) = clickListener(weather)
    }
}