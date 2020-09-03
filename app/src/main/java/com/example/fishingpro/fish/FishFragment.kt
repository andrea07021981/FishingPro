package com.example.fishingpro.fish

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fishingpro.databinding.FragmentFishBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FishFragment() : Fragment() {

    companion object {
        val TAG = FishFragment::class.java.simpleName
    }

    private val argUserId: FishFragmentArgs by navArgs()

    private val fishViewModel: FishViewModel by viewModels()
    private lateinit var dataBinding: FragmentFishBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentFishBinding.inflate(inflater).also {
            it.fishViewModel = fishViewModel
            it.lifecycleOwner = this
            it.fishRecycleView.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
            it.fishRecycleView.adapter = FishAdapter(FishAdapter.OnFishClickListener {
                Log.d(TAG, "Clicked")
            })
        }
        dataBinding.fishToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        fishViewModel.catches.observe(viewLifecycleOwner, {
            print(it)
        })
        return dataBinding.root
    }
}