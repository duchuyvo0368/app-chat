package com.example.appchat.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appchat.R
import com.example.appchat.data.EventObserver
import com.example.appchat.databinding.FragmentStartBinding


class StartFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentStartBinding
    private val viewModel by viewModels<StartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentStartBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        //quan sát dữ liệu LiveData và cập nhập lên UI
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.loginEvent.observe(viewLifecycleOwner, EventObserver {
            navigateToLogin()
        })
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_startFragment_to_loginFragment)
    }
}