package com.example.appchat.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.appchat.AppChat
import com.example.appchat.R
import com.example.appchat.data.EventObserver
import com.example.appchat.data.model.ChatWithUserInfo
import com.example.appchat.databinding.FragmentUsersBinding


class UsersFragment : Fragment() {

    private val viewModel: UsersViewModel by viewModels { UsersViewModelFactory(AppChat.myUserID) }
    private lateinit var viewDataBinding: FragmentUsersBinding
    private lateinit var listAdapter: UserListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentUsersBinding.inflate(layoutInflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner=this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        setupObservers()
    }

    private fun setupObservers() {


    }

    private fun navigationToChat(chatWithUserInfo: ChatWithUserInfo) {
        viewModel.selectUser.observe(viewLifecycleOwner,EventObserver{navigationToProfile(it.info.id)})
    }

    private fun navigationToProfile(userId: String) {
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = UserListAdapter(viewModel)
            viewDataBinding.usersRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not  initialized")
        }
    }


}