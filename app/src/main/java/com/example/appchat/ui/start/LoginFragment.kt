package com.example.appchat.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appchat.R
import com.example.appchat.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var viewDataBinding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.snackBarText.observe(this, Observer {

        })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        viewDataBinding = FragmentLoginBinding.inflate(inflater, container, false)
            .apply { viewmodel=viewModel }
        return viewDataBinding.root
    }

    private fun navigation(){
        findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
    }

}