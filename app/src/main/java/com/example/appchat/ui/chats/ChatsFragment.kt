package com.example.appchat.ui.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.appchat.AppChat
import com.example.appchat.R
import com.example.appchat.databinding.FragmentChatsBinding


class ChatsFragment : Fragment() {
    private val  viewModel:ChatsViewModel by viewModels { ChatsViewModelFactory(AppChat.myUserId) }
    private lateinit var viewDataBinding:FragmentChatsBinding
    private lateinit var listAdapter:ChatsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //inflater:khoi tao layout xml,container:viewgroup chua fragment,
        // false view k duoc inflate container neu true view vao container va hien thi
        viewDataBinding= FragmentChatsBinding.inflate(inflater,container,false).apply { viewmodel=viewModel }
        //thiet lap binding quan ly vong doi
        viewDataBinding.lifecycleOwner=this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
    }

    private fun setupListAdapter() {
        val viewModel=viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter=ChatsListAdapter(viewModel)
            viewDataBinding.chatsRecyclerView.adapter=listAdapter
        }else{
            throw Exception("The viewmodel is not initialized")
        }
    }
}