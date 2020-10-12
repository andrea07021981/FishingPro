package com.example.fishingpro.home

import android.Manifest
import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.example.fishingpro.EventObserver
import com.example.fishingpro.MainActivity
import com.example.fishingpro.R
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.example.fishingpro.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(), MainActivity.OnLocationUpdateDelegate {

    companion object {
        val TAG = HomeFragment::class.java.simpleName
    }
    private val userViewModel: HomeViewModel by viewModels()

    private lateinit var dataBinding: FragmentHomeBinding
    private lateinit var locationManager: LocationManager
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var chart: BarChart


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentHomeBinding.inflate(inflater).also {
            it.lifecycleOwner = this
            it.homeViewModel = userViewModel
            it.maintoolbar.setNavigationOnClickListener {
                userViewModel.logOutUser()
            }
        }
        chart = dataBinding.root.catches_chart
        prepareChart()

        userViewModel.userEvent.observe(this.viewLifecycleOwner, EventObserver {
            //TODO change with user info fragment dialog
            AlertDialog.Builder(requireNotNull(activity))
                .setTitle("Log Out")
                .setMessage("Would you like to log out?")
                .setCancelable(false)
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setPositiveButton("Ok") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    userViewModel.logOutUser()
                }
                .create()
                .show()
        })
        userViewModel.userLogOutEvent.observe(viewLifecycleOwner, EventObserver {
            it.let { out ->
                if (out) {
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireNotNull(activity), "Error log out", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
        userViewModel.weatherEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                val weatherImage =
                    dataBinding.weatherImageView
                val weatherTemperature =
                    dataBinding.temperatureTextView
                val extras = FragmentNavigatorExtras(
                    weatherImage to "weatherImage",
                    weatherTemperature to "weatherTemperature"
                )
                val bundle = bundleOf("localWeatherDomain" to it)
                findNavController()
                    .navigate(R.id.weatherFragment, bundle, null, extras)
            }
        })
        userViewModel.fishEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController()
                .navigate(R.id.fishFragment, bundleOf("userId" to it))
        })
        userViewModel.catches.observe(viewLifecycleOwner, Observer {
            //Update chart data
            setData(it)
        })
        locationManager = (requireNotNull(activity).getSystemService(LOCATION_SERVICE) as LocationManager?)!!
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(
            requireNotNull(
                activity
            )
        )
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        dataBinding.mainappbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset -> //  Vertical offset == 0 indicates appBar is fully expanded.
            val offsetAlpha = appBarLayout.y / appBarLayout.totalScrollRange
            val value = 0 + (255 * offsetAlpha * -1).toInt()
            Log.d(TAG, "Value: $value")
            appBarLayout.maintoolbar.navigationIcon?.alpha = value
        })

        // Mange the back device button
        requireNotNull(activity).onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //TODO manage here or in activity
            }

        })
        return dataBinding.root
    }

    //TODO refactor it
    private fun prepareChart() {
        val xAxis: XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        val months = listOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
        val formatter = IndexAxisValueFormatter(months)
        xAxis.granularity = 1f
        xAxis.valueFormatter = formatter

        val yAxis = chart.axisLeft;
        yAxis.textSize = 12f; // set the text size
        yAxis.axisMinimum = 0f; // start at zero
        yAxis.axisMaximum = 100f; // the axis maximum is 100
        yAxis.textColor = Color.BLACK;
        yAxis.granularity = 1f; // interval 1

        //Hide right y label
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.setFitBars(true)
    }

    private fun setData(dataCollection: List<LocalDailyCatch>) { // now in hours
        val catches = arrayListOf<BarEntry>()
        for (data in dataCollection.groupBy {
            val current = Calendar.getInstance()
            current.time = it.date ?: Date()
            current.get(Calendar.MONTH)
        }) {
            catches.add(BarEntry(data.key.toFloat(), data.value.count().toFloat()))
        }

        //Change the yAxis max and count
        with(catches.maxOf { it.y }) {
            chart.axisLeft.mAxisMaximum = toFloat()
            chart.axisLeft.setLabelCount(this.toInt(), true); // force 6 labels
        }
        val barDataSet = BarDataSet(catches, "Catches")
        barDataSet.barBorderWidth = 0.1f
        barDataSet.colors = ColorTemplate.COLORFUL_COLORS.asList()
        val barData = BarData(barDataSet)
        chart.data = barData
        chart.animateXY(5000, 5000)
        chart.invalidate()
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
            Toast.makeText(
                requireNotNull(activity),
                "Location must be granted for this app",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        fusedLocationClient!!.lastLocation
            .addOnCompleteListener(
                requireNotNull(activity)
            ) { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        userViewModel.updateLatLong(it.latitude, it.longitude)
                    }
                } else {
                    Toast.makeText(
                        requireNotNull(activity),
                        "Location must be granted for this app",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        (requireNotNull(activity) as MainActivity).mainDelegate = this
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireNotNull(activity) as MainActivity).mainDelegate = null
    }

    override fun updateLocation(lastLocation: Location) {
        Log.d(TAG, "Location retrieved: ${lastLocation.latitude}, ${lastLocation.longitude}")
        // TODO remove the other location, but we need lat long as soons as we enter here
        userViewModel.updateLatLong(lastLocation.latitude, lastLocation.longitude)
    }
}