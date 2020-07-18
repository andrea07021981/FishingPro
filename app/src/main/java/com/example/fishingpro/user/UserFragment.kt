package com.example.fishingpro.user

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fishingpro.EventObserver
import com.example.fishingpro.MainActivity
import com.example.fishingpro.R
import com.example.fishingpro.data.source.repository.WeatherDataRepository
import com.example.fishingpro.databinding.FragmentUserBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class UserFragment : Fragment() {

    private val userViewModel by viewModels<UserViewModel> {
        UserViewModel.UserViewModelFactory(WeatherDataRepository.getRepository(requireNotNull(activity).application))
    }

    private lateinit var dataBinding: FragmentUserBinding
    private lateinit var locationManager: LocationManager
    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentUserBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.userViewModel = userViewModel
        dataBinding.maintoolbar.setNavigationOnClickListener {
            userViewModel.backToRecipeList()
        }
        userViewModel.userEvent.observe(this.viewLifecycleOwner, EventObserver {
            findNavController().popBackStack()
        })
        locationManager = (requireNotNull(activity).getSystemService(LOCATION_SERVICE) as LocationManager?)!!
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireNotNull(activity))
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                requireNotNull(activity),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireNotNull(activity),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireNotNull(activity), "Location must be granted for this app", Toast.LENGTH_LONG).show()
            return
        }
        fusedLocationClient!!.lastLocation
            .addOnCompleteListener(
                requireNotNull(activity)
            ) { task ->
                if (task.isSuccessful && task.result != null) {
                    task.result?.let {
                        userViewModel.updateLatLong(it.latitude, it.longitude)
                    }
                } else {
                    Toast.makeText(requireNotNull(activity), "Location must be granted for this app", Toast.LENGTH_LONG).show()
                }
            }
    }
}