package com.example.fishingpro.login

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import br.com.simplepass.loadingbutton.animatedDrawables.ProgressType
import br.com.simplepass.loadingbutton.customViews.ProgressButton
import com.example.fishingpro.R
import com.example.fishingpro.constant.*
import com.example.fishingpro.data.source.repository.UserDataRepository
import com.example.fishingpro.data.source.repository.UserRepository
import com.example.fishingpro.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.LoginViewModelFactory(UserDataRepository.getRepository(requireNotNull(activity).application))
    }

    lateinit var dataBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentLoginBinding.inflate(inflater)
        dataBinding.loginViewModel = loginViewModel
        dataBinding.lifecycleOwner = this
        loginViewModel.loginAuthenticationState.observe(this.viewLifecycleOwner, Observer {
            if (it is Authenticated || it is Authenticating || it is InvalidAuthentication) {
                dataBinding.loginButton.run { morphDoneAndRevert(requireNotNull(activity), it) }
            } else if (it is Unauthenticated){
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }
        })
        return dataBinding.root
    }

    private fun ProgressButton.morphDoneAndRevert(
        context: Context,
        state: LoginAuthenticationStates?,
        fillColor: Int = ContextCompat.getColor(context, R.color.customGreen),
        bitmap: Bitmap = defaultDoneImage(context.resources),
        doneTime: Long = 3000,
        navigateTime: Long = 1000,
        revertTime: Long = 4000
    ) {
        progressType = ProgressType.INDETERMINATE

        when (state) {
            is Authenticating -> {
                startAnimation()
            }
            is Authenticated -> {
                Handler().run {
                    doneLoadingAnimation(fillColor, bitmap)
                    postDelayed({
                        /*
                        val bundle = bundleOf("x" to dataBinding.loginButton.x, "y" to dataBinding.loginButton.y)
                        bundle.putParcelable("user", state.user)
                        findNavController()
                            .navigate(
                                LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                                    bundle
                                )
                            )
                         */
                        }, navigateTime)
                    loginViewModel.resetState()
                }
            }
            is Unauthenticated -> {
                Handler().run {
                    postDelayed(::revertAnimation, revertTime)
                }
            }
            is InvalidAuthentication -> {
                val fillColorError = ContextCompat.getColor(context, R.color.customRed)
                val bitmapError: Bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_check_ko)
                Handler().run {
                    postDelayed({ doneLoadingAnimation(fillColorError, bitmapError) }, doneTime)
                    postDelayed(::revertAnimation, revertTime)
                    postDelayed({toastMessage(state)}, 1000)
                }
            }
        }

    }

    private fun toastMessage(state: LoginAuthenticationStates)= Toast.makeText(requireActivity(), state.message, Toast.LENGTH_SHORT).show()

    private fun defaultDoneImage(resources: Resources) =
        BitmapFactory.decodeResource(resources, R.mipmap.ic_check_ko)
}