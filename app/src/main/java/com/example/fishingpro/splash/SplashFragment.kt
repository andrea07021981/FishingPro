package com.example.fishingpro.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fishingpro.R
import com.hanks.htextview.base.AnimationListener
import com.hanks.htextview.evaporate.EvaporateTextView
import com.hanks.htextview.line.LineTextView

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_splash, container, false)
        val splashTextView: LineTextView = root.findViewById(R.id.splash_textview)
        splashTextView.animateText("Fishing pro")
        return root
    }
}