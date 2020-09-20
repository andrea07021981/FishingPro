package com.example.fishingpro.catchdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fishingpro.data.domain.FishData
import com.example.fishingpro.databinding.FragmentCatchDetailBinding
import com.example.fishingpro.util.toLatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CatchDetailFragment : Fragment() {

    private lateinit var map: GoogleMap
    private val catchDetailViewModel: CatchDetailViewModel by viewModels()
    private lateinit var dataBinding: FragmentCatchDetailBinding
    private lateinit var catchDetails: ArrayList<FishData?>

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
                catchDetails.forEach { fishData ->
                    map.addMarker(
                        fishData?.location?.toLatLng()?.let { location ->
                            MarkerOptions()
                                .position(location)
                                .title(fishData.fishId)
                        }
                    )
                }
                //TODO calculate the average distance and zoom on it
                map.moveCamera(CameraUpdateFactory.newLatLng(catchDetails[0]?.location?.toLatLng()))
            }
            it.catchtoolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        catchDetailViewModel.catchInfo.observe(viewLifecycleOwner) {
            it?.let {
                catchDetails = it.fish.toMutableList() as ArrayList<FishData?>
            }
        }
        return dataBinding.root
    }
}