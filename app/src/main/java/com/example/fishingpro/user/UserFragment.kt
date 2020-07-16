package com.example.fishingpro.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.fishingpro.EventObserver
import com.example.fishingpro.R
import com.example.fishingpro.databinding.FragmentUserBinding

class UserFragment : Fragment() {

    private val userViewModel by viewModels<UserViewModel> {
        UserViewModel.UserViewModelFactory()
    }

    private lateinit var dataBinding: FragmentUserBinding

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
        return dataBinding.root
    }
}