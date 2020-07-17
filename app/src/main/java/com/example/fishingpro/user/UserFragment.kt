package com.example.fishingpro.user

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.fishingpro.EventObserver
import com.example.fishingpro.R
import com.example.fishingpro.data.source.repository.WeatherDataRepository
import com.example.fishingpro.data.source.repository.WeatherRepository
import com.example.fishingpro.databinding.FragmentUserBinding

class UserFragment : Fragment() {

    private val userViewModel by viewModels<UserViewModel> {
        UserViewModel.UserViewModelFactory(WeatherDataRepository.getRepository(requireNotNull(activity).application))
    }

    private lateinit var dataBinding: FragmentUserBinding
    private lateinit var locationManager: LocationManager

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
        //TODO request permissions
        return dataBinding.root
    }
}