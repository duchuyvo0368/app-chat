package com.example.appchat.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appchat.R
import com.example.appchat.data.EventObserver
import com.example.appchat.databinding.FragmentStartBinding
import com.example.appchat.utils.SharedPreferencesUtil


class StartFragment : Fragment() {

    private val viewModel by viewModels<StartViewModel>()
    private lateinit var viewDataBinding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentStartBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()

        if (userIsAlreadyLoggedIn()) {
            navigateDirectlyToChats()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private fun userIsAlreadyLoggedIn(): Boolean {
        return SharedPreferencesUtil.getUserID(requireContext()) != null
    }

    private fun setupObservers() {
        viewModel.loginEvent.observe(viewLifecycleOwner, EventObserver { navigateToLogin() })
        viewModel.createAccountEvent.observe(
            viewLifecycleOwner, EventObserver { navigateToCreateAccount() })
    }

    private fun navigateDirectlyToChats() {
        findNavController().navigate(R.id.action_startFragment_to_navigation_chats)
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_startFragment_to_loginFragment)
    }

    private fun navigateToCreateAccount() {
        findNavController().navigate(R.id.action_startFragment_to_createAccountFragment)
    }
}