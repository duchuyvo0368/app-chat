package com.example.appchat.ui.start.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appchat.R
import com.example.appchat.data.EventObserver
import com.example.appchat.databinding.FragmentLoginBinding
import com.example.appchat.utils.SharedPreferencesUtil
import com.example.appchat.utils.forceHideKeyboard
import com.example.appchat.utils.showSnackBar
import com.example.appchat.ui.main.MainActivity

class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var viewDataBinding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentLoginBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        setHasOptionsMenu(true)
       val supportActionBar = (activity as AppCompatActivity?)!!.supportActionBar
        supportActionBar!!.hide()
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }



    private fun setupObservers() {
        viewModel.dataLoading.observe(viewLifecycleOwner,
            EventObserver { (activity as MainActivity).showGlobalProgressBar(it) })

        viewModel.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
                view?.forceHideKeyboard()
            })

        viewModel.isLoggedInEvent.observe(viewLifecycleOwner, EventObserver {
            SharedPreferencesUtil.saveUserID(requireContext(), it.uid)
            navigateToChats()
        })
    }

    override fun onDestroy() {
        val supportActionBar = (activity as AppCompatActivity?)!!.supportActionBar
        supportActionBar!!.show()
        super.onDestroy()
    }

    private fun navigateToChats() {
        findNavController().navigate(R.id.action_loginFragment_to_navigation_chats)
    }
}