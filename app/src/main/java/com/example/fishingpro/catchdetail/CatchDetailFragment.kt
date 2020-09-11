package com.example.fishingpro.catchdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fishingpro.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback


class CatchDetailFragment : Fragment() {

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catch_detail, container, false)
        val mapView = view.findViewById(R.id.fish_mapview) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it;
            map.uiSettings.isMyLocationButtonEnabled = false;
        }
        return view
    }
}