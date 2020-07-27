package com.example.fishingpro.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.fishingpro.R
import com.example.fishingpro.data.source.repository.WeatherDataRepository
import com.example.fishingpro.data.source.repository.WeatherRepository
import com.example.fishingpro.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {

    private val weatherDomain: WeatherFragmentArgs by navArgs()

    private val weatherViewModel by viewModels<WeatherViewModel> {
        WeatherViewModel.WeatherViewModelFactory(WeatherDataRepository.getRepository(requireNotNull(activity).application), weatherDomain.localWeatherDomain)
    }
    private lateinit var dataBinding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentWeatherBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.weatherViewModel = weatherViewModel
        //TODO check time to load image
        dataBinding.forecastRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        dataBinding.forecastRecyclerView.adapter = WeatherAdapter(
            WeatherAdapter.OnWeatherClickListener {
                print("Selected weather ${it.wId}")
            })
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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