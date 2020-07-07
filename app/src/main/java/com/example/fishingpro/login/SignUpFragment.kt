package com.example.fishingpro.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fishingpro.EventObserver
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