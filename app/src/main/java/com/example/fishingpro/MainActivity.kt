package com.example.fishingpro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //TODO review the class, it could be an abstract parent of home
    private lateinit var firebaseConfig: FirebaseRemoteConfig

    interface OnLocationUpdateDelegate {
        fun updateLocation(lastLocation: Location)
    }

    var mainDelegate: OnLocationUpdateDelegate? = null

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 600000
        /**
         * The fastest rate for active location updates. Exact. Updates will never be more frequent
         * than this value.
         */
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    }

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationSettingRequest: LocationSettingsRequest
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFirebaseConfig()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        settingsClient = LocationServices.getSettingsClient(this)
        createCallback()
        createRequest()
        createSettings()
        startTrackingApp()
    }

    private fun createRequest() {
        locationRequest = LocationRequest().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
//TODO add bottomnav with navigation comnent https://github.com/android/architecture-components-samples/blob/master/NavigationAdvancedSample/app/src/main/java/com/example/android/navigationadvancedsample/MainActivity.kt
    private fun createSettings() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        locationSettingRequest = builder.build()
    }

    private fun createCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                locationResult ?: return
                //showSnackbar(getString(R.string.location_detected).plus(lastLocation))
                //Update whoever is listening
                mainDelegate?.let {
                    it.updateLocation(locationResult.lastLocation)
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
                showSnackbar("Location available")
            }
        }
    }

    /**
     * Load the remote configuration from Firebase
     */
    private fun loadFirebaseConfig() {
        //Load the default remote config
        firebaseConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG)  1 else 3600
        }
        firebaseConfig.setConfigSettingsAsync(configSettings).addOnCompleteListener {
            if (it.isSuccessful) {
                firebaseConfig.setDefaultsAsync(R.xml.remote_config_defaults).addOnCompleteListener {
                    if (it.isSuccessful) {
                        firebaseConfig.fetchAndActivate()
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    val updated = task.result
                                    Log.d(TAG, "Config params updated: $updated")
                                    Toast.makeText(
                                        this, "Fetch and activate succeeded",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this, "Fetch failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
            }
        }
    }

    private fun startTrackingApp() {
        // Microsoft App Config
        AppCenter.start(
            application, "95a1ea7a-69cf-44fa-a7ad-526fb81a4498",
            Analytics::class.java, Crashes::class.java
        )
        //Track the event of app starting
        Analytics.trackEvent("Application started");
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     *
     *
     * Note: this method should be called after location permission has been granted.
     */
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions()
            return
        }

        //Start requesting updates
        settingsClient.let {
            it.checkLocationSettings(locationSettingRequest)
                .addOnSuccessListener {
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )
                }
                .addOnFailureListener {
                    showSnackbar("Location permission must be granted")
                }
        }
    }

    /**
     * Shows a [Snackbar] using `text`.
     *
     * @param text The Snackbar text.
     */
    private fun showSnackbar(text: String) {
        val container =
            findViewById<View>(R.id.main_content)
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * Shows a [Snackbar].
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private fun showSnackbar(
        mainTextStringId: Int, actionStringId: Int,
        listener: View.OnClickListener
    ) {
        Snackbar.make(
            findViewById(android.R.id.content),
            getString(mainTextStringId),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(actionStringId), listener).show()
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    /**
     * Request location permission for localization
     */
    private fun requestPermissions() {
        val shouldProvideRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(
                TAG,
                "Displaying permission rationale to provide additional context."
            )
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                View.OnClickListener { // Request permission
                    startLocationPermissionRequest()
                })
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest()
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == Companion.REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(Companion.TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    startLocationUpdates()
                }
                else -> {
                    // Permission denied.

                    // Notify the user via a SnackBar that they have rejected a core permission for the
                    // app, which makes the Activity useless. In a real app, core permissions would
                    // typically be best requested during a welcome-screen flow.

                    // Additionally, it is important to remember that a permission might have been
                    // rejected without asking the user for permission (device policy or "Never ask
                    // again" prompts). Therefore, a user interface affordance is typically implemented
                    // when permissions are denied. Otherwise, your app could appear unresponsive to
                    // touches or interactions which have required permissions.
                    showSnackbar(R.string.permission_denied_explanation, R.string.action_settings,
                        View.OnClickListener { // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        })
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}