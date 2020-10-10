package com.example.fishingpro.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fishingpro.R
import com.example.fishingpro.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MapFragment : Fragment() {

    companion object {
        private val TAG = MapFragment::class.java.simpleName
    }

    private val mapViewModel: MapViewModel by viewModels()

    private lateinit var dataBinding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentMapBinding.inflate(inflater).also {
            it.mapViewModel = mapViewModel
            it.lifecycleOwner = this
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { map ->
            mapViewModel.catches.observe(viewLifecycleOwner) { markerList ->
                map?.let { map ->
                    markerList.forEach {
                        map.addMarker(it)
                    }
                    //TODO add info and click for markers
                }
            }
            map.moveCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        44.394473,
                        -79.680223
                    )
                )
            ) //Fixed for now
            //TODO calculate the average disntance among points for zoom
        }

        return dataBinding.root
    }
}