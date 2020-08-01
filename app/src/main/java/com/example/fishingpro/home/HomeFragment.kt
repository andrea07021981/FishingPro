package com.example.fishingpro.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.example.fishingpro.R
import com.example.fishingpro.map.MapFragment
import com.example.fishingpro.user.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig


class HomeFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val navigation: BottomNavigationView = view.findViewById(R.id.homeBottomNavigation)
        navigation.setOnNavigationItemSelectedListener(this)

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val mapVisible = firebaseRemoteConfig.getBoolean("map_visibility")
        //TODO check why it doesn't read the correct server value
        navigation.menu.findItem(R.id.map_page).isVisible = mapVisible
        loadFragment(UserFragment())
        return view
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return loadFragment(when (item.itemId) {
                R.id.user_page -> HomeFragment()
                R.id.map_page -> MapFragment()
                else -> null
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }
    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        fragment?.let {
            requireNotNull(activity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.home_container, fragment)
                .commit()
            return true
        }
        return false
    }
}