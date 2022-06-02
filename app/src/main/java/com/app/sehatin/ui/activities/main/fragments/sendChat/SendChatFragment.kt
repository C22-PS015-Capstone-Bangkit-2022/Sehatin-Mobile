package com.app.sehatin.ui.activities.main.fragments.sendChat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.R
import com.app.sehatin.databinding.FragmentSendChatBinding
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class SendChatFragment : Fragment() {
    private lateinit var binding: FragmentSendChatBinding
    private lateinit var viewModel: SendChatViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSendChatBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance())[SendChatViewModel::class.java]
    }

    private fun initListener() {

    }

}