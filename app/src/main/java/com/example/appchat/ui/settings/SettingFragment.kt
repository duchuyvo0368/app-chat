package com.example.appchat.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appchat.AppChat
import com.example.appchat.R
import com.example.appchat.data.EventObserver
import com.example.appchat.databinding.FragmentProfileBinding
import com.example.appchat.databinding.FragmentSettingBinding
import com.example.appchat.ui.profile.ProfileViewModel
import com.example.appchat.ui.profile.ProfileViewModelFactory
import com.example.appchat.utils.forceHideKeyboard
import com.example.appchat.utils.showSnackBar
import com.example.appchat.ui.main.MainActivity
import com.example.appchat.utils.SharedPreferencesUtil
import com.example.appchat.utils.convertFileToByteArray


class SettingFragment : Fragment() {



    private val viewModel: SettingsViewModel by viewModels { SettingsViewModelFactory(AppChat.myUserID) }

    private lateinit var viewDataBinding: FragmentSettingBinding
    private val selectImageIntentRequestCode = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentSettingBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == selectImageIntentRequestCode) {
            data?.data?.let { uri ->
                convertFileToByteArray(requireContext(), uri).let {
                    viewModel.changeUserImage(it)
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.editStatusEvent.observe(viewLifecycleOwner,
            EventObserver { showEditStatusDialog() })

        viewModel.editImageEvent.observe(viewLifecycleOwner,
            EventObserver { startSelectImageIntent() })

        viewModel.logoutEvent.observe(viewLifecycleOwner,
            EventObserver {
                SharedPreferencesUtil.removeUserID(requireContext())
                navigateToStart()
            })
    }

    private fun showEditStatusDialog() {
        val input = EditText(requireActivity() as Context)
        AlertDialog.Builder(requireActivity()).apply {
            setTitle("Status:")
            setView(input)
            setPositiveButton("Ok") { _, _ ->
                val textInput = input.text.toString()
                if (!textInput.isBlank() && textInput.length <= 40) {
                    viewModel.changeUserStatus(textInput)
                }
            }
            setNegativeButton("Cancel") { _, _ -> }
            show()
        }
    }

    private fun startSelectImageIntent() {
        val selectImageIntent = Intent(Intent.ACTION_GET_CONTENT)
        selectImageIntent.type = "image/*"
        startActivityForResult(selectImageIntent, selectImageIntentRequestCode)
    }

    private fun navigateToStart() {
        findNavController().navigate(R.id.action_navigation_settings_to_startFragment)
    }
}