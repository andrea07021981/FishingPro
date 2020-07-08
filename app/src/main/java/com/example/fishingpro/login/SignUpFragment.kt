package com.example.fishingpro.login

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fishingpro.EventObserver
import com.example.fishingpro.R
import com.example.fishingpro.constant.Authenticated
import com.example.fishingpro.constant.Authenticating
import com.example.fishingpro.constant.InvalidAuthentication
import com.example.fishingpro.constant.Unauthenticated
import com.example.fishingpro.data.source.repository.UserDataRepository
import com.example.fishingpro.databinding.FragmentSignupBinding

class SignUpFragment : Fragment() {

    //We can use by viewModels when the VM is not shared with other fragments
    private val signUpViewModel by viewModels<SignUpViewModel>() {
        SignUpViewModel.SignUpViewModelFactory(UserDataRepository.getRepository(requireActivity().application))
    }

    private lateinit var dataBinding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding = FragmentSignupBinding.inflate(inflater)
        dataBinding.signupViewModel = signUpViewModel
        dataBinding.lifecycleOwner = this
        signUpViewModel.loginAuthenticationState.observe(this.viewLifecycleOwner, Observer {
            when (it) {
                is Authenticating -> {
                    Toast.makeText(requireNotNull(activity), "Creating user", Toast.LENGTH_SHORT).show()
                }
                is Authenticated -> {
                    Toast.makeText(requireNotNull(activity), "Creating user", Toast.LENGTH_SHORT).show()
                }
                is Unauthenticated, is InvalidAuthentication -> {
                    Toast.makeText(requireNotNull(activity), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        signUpViewModel.loginEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                this
                    .findNavController()
                    .popBackStack()
            }
        })
    }
}