package com.example.fishingpro.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.fishingpro.databinding.FragmentWeatherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val weatherDomain: WeatherFragmentArgs by navArgs()

    private val weatherViewModel: WeatherViewModel by viewModels()

    private lateinit var dataBinding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentWeatherBinding.inflate(inflater).also {
            it.lifecycleOwner = this
            it.weatherViewModel = weatherViewModel
            //TODO check time to load image
            it.forecastRecyclerView.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            it.forecastRecyclerView.adapter = WeatherAdapter(
                WeatherAdapter.OnWeatherClickListener { localWeather ->
                    print("Selected weather ${localWeather.wId}")
                })
        }
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityNavigator.applyPopAnimationsToPendingTransition(requireNotNull(activity))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ActivityNavigator.applyPopAnimationsToPendingTransition(requireNotNull(activity))
    }
}