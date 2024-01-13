package com.example.appchat.ui.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appchat.AppChat
import com.example.appchat.R
import com.example.appchat.data.EventObserver
import com.example.appchat.data.model.ChatWithUserInfo
import com.example.appchat.databinding.FragmentChatsBinding
import com.example.appchat.utils.convertTowUserIDs
import com.example.appchat.ui.chat.ChatFragment


class ChatsFragment : Fragment() {
    private val viewModel: ChatsViewModel by viewModels { ChatsViewModelFactory(AppChat.myUserID) }
    private lateinit var viewDataBinding: FragmentChatsBinding
    private lateinit var listAdapter: ChatsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentChatsBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        setupObservers()
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = ChatsListAdapter(viewModel)
            viewDataBinding.chatsRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    private fun setupObservers() {
        viewModel.selectedChat.observe(viewLifecycleOwner,
            EventObserver { navigateToChat(it) })
    }

    private fun navigateToChat(chatWithUserInfo: ChatWithUserInfo) {
        val bundle = bundleOf(
            ChatFragment.ARGS_KEY_USER_ID to AppChat.myUserID,
            ChatFragment.ARGS_KEY_OTHER_USER_ID to chatWithUserInfo.myInfo.id,
            ChatFragment.ARGS_KEY_CHAT_ID to convertTowUserIDs(AppChat.myUserID, chatWithUserInfo.myInfo.id)
        )
        findNavController().navigate(R.id.action_navigation_chats_to_chatFragment, bundle)
    }
}