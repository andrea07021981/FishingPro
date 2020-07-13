package com.example.fishingpro.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fishingpro.R
import com.example.fishingpro.map.MapFragment
import com.example.fishingpro.user.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val navigation: BottomNavigationView = view.findViewById(R.id.homeBottomNavigation)
        navigation.setOnNavigationItemSelectedListener(this)
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

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            requireNotNull(activity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.home_container, fragment)
                .commit()
            return true
        }
        return false
    }
}