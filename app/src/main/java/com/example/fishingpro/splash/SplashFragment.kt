package com.example.fishingpro.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fishingpro.R
import com.example.fishingpro.databinding.FragmentSplashBinding
import com.hanks.htextview.rainbow.RainbowTextView

class SplashFragment : Fragment() {

    private val splashViewModel by viewModels<SplashViewModel>()
    private lateinit var dataBinding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentSplashBinding.inflate(inflater)
        dataBinding.splashViewModel = splashViewModel
        dataBinding.lifecycleOwner = this
        splashViewModel.loginEvent.observe(this.viewLifecycleOwner, Observer {
            it.let {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        })
        return dataBinding.root
    }
}