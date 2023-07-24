package com.example.appchat.ui.start.createAccount


import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appchat.R
import com.example.appchat.data.EventObserver
import com.example.appchat.databinding.FragmentRegisterBinding
import com.example.appchat.showSnackBar
import com.example.appchat.ui.SharedPreferencesUtil
import com.example.appchat.ui.main.MainActivity



class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
            .apply { viewmodel=viewModel }
        //quan sát dữ liệu LiveData và cập nhập lên UI
        binding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.dataLoading.observe(
            viewLifecycleOwner,
            EventObserver { (activity as MainActivity).showGlobalProgressBar(it) }
        )
        viewModel.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
            })
        viewModel.passwordStrength.observe(viewLifecycleOwner) {
        }
        viewModel.loginAccount.observe(viewLifecycleOwner,EventObserver{
            navigationLogin()
        })
        viewModel.isCreateEvent.observe(viewLifecycleOwner,EventObserver{
            SharedPreferencesUtil.saveUserID(requireContext(),it.uid)
            navigationChats()
        })
    }






    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_close -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.button_close, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun navigationLogin(){
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }
    private fun navigationChats() {
        findNavController().navigate(R.id.action_registerFragment_to_chatsFragment)
    }

}