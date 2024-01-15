package com.example.appchat.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.appchat.AppChat
import com.example.appchat.R
import com.example.appchat.data.EventObserver
import com.example.appchat.databinding.FragmentProfileBinding
import com.example.appchat.utils.forceHideKeyboard
import com.example.appchat.utils.showSnackBar
import com.example.appchat.ui.main.MainActivity


class ProfileFragment : Fragment() {

    companion object {
        const val ARGS_KEY_USER_ID = "bundle_user_id"
    }

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(AppChat.myUserID, requireArguments().getString(ARGS_KEY_USER_ID)!!)
    }

    private lateinit var viewDataBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentProfileBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
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
    }
}