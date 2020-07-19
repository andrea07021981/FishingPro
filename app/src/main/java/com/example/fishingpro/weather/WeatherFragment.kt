package com.example.fishingpro.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.ActivityNavigator
import androidx.transition.TransitionInflater
import com.example.fishingpro.R

class WeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //TODO look up shared elements with glide
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityNavigator.applyPopAnimationsToPendingTransition(requireNotNull(activity))
    }
}