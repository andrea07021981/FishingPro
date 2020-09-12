package com.example.fishingpro.catchdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fishingpro.R
import com.example.fishingpro.databinding.FragmentCatchDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_catch_detail.*


@AndroidEntryPoint
class CatchDetailFragment : Fragment() {

    private lateinit var map: GoogleMap
    private val catchDetailViewModel: CatchDetailViewModel by viewModels()
    private lateinit var dataBinding: FragmentCatchDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentCatchDetailBinding.inflate(inflater).also {
            it.catchDetailViewModel = catchDetailViewModel
            it.lifecycleOwner = this
            it.fishMapview.onCreate(savedInstanceState)
            it.fishMapview.onResume()
            it.fishMapview.getMapAsync { googleMap ->
                map = googleMap

                // Add a marker in Sydney and move the camera
                val sydney = LatLng(-34.0, 151.0)
                map.addMarker(
                    MarkerOptions()
                        .position(sydney)
                        .title("Marker in Sydney")
                )
                map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            }
        }

        return view
    }
}