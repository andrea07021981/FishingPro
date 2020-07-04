package com.example.fishingpro.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.fishingpro.R
import com.hanks.htextview.base.AnimationListener
import com.hanks.htextview.evaporate.EvaporateTextView
import com.hanks.htextview.line.LineTextView
import com.hanks.htextview.rainbow.RainbowTextView

class SplashFragment : Fragment() {

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_splash, container, false)
        //TODO change splash effect and add viewmodels and a timer to exit. Add databinding and move animation in bindingadapter
        val splashTextView: RainbowTextView = root.findViewById(R.id.splash_textview)
        //splashTextView.setColors(android.R.color.black)
        splashTextView.animateText("Fishing pro")
        splashViewModel.loginEvent.observe(this.viewLifecycleOwner, Observer {
            it.let {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        })
        return root
    }
}