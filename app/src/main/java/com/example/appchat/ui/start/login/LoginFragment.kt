package com.example.appchat.ui.start.login

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

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
        setHasOptionsMenu(true)
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
                navigationToChats()
        })
        //show message
        viewModel.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
            })
        viewModel.createAccount.observe(viewLifecycleOwner,EventObserver{
            navigationCreateAccount()
        })
        viewModel.rememberMe.observe(viewLifecycleOwner,EventObserver{ rememberMe ->

        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
             R.id.menu_close-> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.button_close,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }



    private fun navigationToChats() {
        findNavController().navigate(R.id.action_loginFragment_to_chatsFragment)
    }



    private fun navigationCreateAccount(){
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }


}