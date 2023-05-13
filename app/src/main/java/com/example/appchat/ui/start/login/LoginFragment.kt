package com.example.appchat.ui.start.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appchat.R
import com.example.appchat.data.EventObserver
import com.example.appchat.databinding.FragmentLoginBinding
import com.example.appchat.showSnackBar
import com.example.appchat.ui.main.MainActivity


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
            .apply { viewmodel = viewModel }
        //quan sát dữ liệu LiveData và cập nhập lên UI
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {


        
        viewModel.dataLoading.observe(
            viewLifecycleOwner,
            EventObserver {
                (activity as MainActivity).showGlobalProgressBar(it)
            }
        )

        //đăng nhập thành công
        viewModel.isLoggedInEvent.observe(viewLifecycleOwner,
            EventObserver {
                navigation()
        })
        //show message
        viewModel.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
            })
    }

    private fun navigation() {
        findNavController().navigate(R.id.action_loginFragment_to_chatsFragment)
    }

}