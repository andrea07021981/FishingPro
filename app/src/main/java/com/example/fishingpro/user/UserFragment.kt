package com.example.fishingpro.user

import android.Manifest
import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.example.fishingpro.EventObserver
import com.example.fishingpro.R
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.example.fishingpro.databinding.FragmentUserBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class UserFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()

    private lateinit var dataBinding: FragmentUserBinding
    private lateinit var locationManager: LocationManager
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var chart: LineChart
    //TODO change to barchar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentUserBinding.inflate(inflater).also {
            it.lifecycleOwner = this
            it.userViewModel = userViewModel
            it.maintoolbar.setNavigationOnClickListener {
                userViewModel.backToLogin()
            }
        }
        chart = dataBinding.root.catches_chart
        prepareChart()

        userViewModel.userEvent.observe(this.viewLifecycleOwner, EventObserver {
            AlertDialog.Builder(requireNotNull(activity))
                .setTitle("Log Out")
                .setMessage("Would you like to log out?")
                .setCancelable(false)
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setPositiveButton("Ok") {dialogInterface, _ ->
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
                    Toast.makeText(requireNotNull(activity), "Error log out", Toast.LENGTH_LONG).show()
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
                    weatherTemperature to "weatherTemperature")
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireNotNull(activity))
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        return dataBinding.root
    }

    //TODO refactor it
    fun prepareChart() {
        chart.animateX(1500)
        // no description text
        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        // enable touch gestures
        chart.setTouchEnabled(true)

        chart.dragDecelerationFrictionCoef = 0.9f

        // enable scaling and dragging
        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)
        chart.isHighlightPerDragEnabled = true

        // set an alternative background color
        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE)
        chart.setViewPortOffsets(0f, 0f, 0f, 0f)

        // get the legend (only possible after setting data)
        // get the legend (only possible after setting data)
        val l = chart.legend
        l.isEnabled = false

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.TOP_INSIDE
        xAxis.textSize = 10f
        xAxis.textColor = Color.WHITE
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(true)
        xAxis.textColor = ContextCompat.getColor(requireActivity().applicationContext, R.color.primaryColor)
        xAxis.setCenterAxisLabels(true)
        xAxis.granularity = 1f // one hour


        xAxis.valueFormatter = object : ValueFormatter() {
            private val mFormat: SimpleDateFormat = SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH)
            override fun getFormattedValue(value: Float): String {
                val millis: Long = TimeUnit.HOURS.toMillis(value.toLong())
                return mFormat.format(Date(millis))
            }
        }

        val leftAxis = chart.axisLeft
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 100F
        leftAxis.yOffset = -9f
        leftAxis.textColor = ContextCompat.getColor(requireActivity().applicationContext, R.color.primaryColor)

        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
    }

    private fun setData(dataCollection: List<LocalDailyCatch?>) { // now in hours
        val catches: ArrayList<Entry> = ArrayList()
        for (data in dataCollection) {
            val y = data?.fish?.count() ?: 0
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val timeCollection = data?.date
            val temp: Long = (timeCollection?.time ?: 0) / (1000*60*60)
            catches.add(Entry(temp.toFloat(), y.toFloat())) // add one entry per hour
        }

        // create a data object with the data sets
        val chartData = LineData();
        chartData.setValueTextColor(Color.WHITE)
        chartData.setValueTextSize(9f)

        val setTemp = LineDataSet(catches, "DataSet temp")
        setTemp.axisDependency = YAxis.AxisDependency.LEFT
        setTemp.color = ColorTemplate.getHoloBlue()
        setTemp.valueTextColor = ColorTemplate.getHoloBlue()
        setTemp.lineWidth = 1.5f
        setTemp.setDrawCircles(false)
        setTemp.setDrawValues(false)
        setTemp.fillAlpha = 65
        setTemp.fillColor = ColorTemplate.getHoloBlue()
        setTemp.highLightColor = Color.rgb(244, 117, 117)
        setTemp.setDrawCircleHole(false)
        chartData.addDataSet(setTemp);

        //Deploy data to the chart
        chart.data = chartData
        chart.animateXY(2000, 2000)
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