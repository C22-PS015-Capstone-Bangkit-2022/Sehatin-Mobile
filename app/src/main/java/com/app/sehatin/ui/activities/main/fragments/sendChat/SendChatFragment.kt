package com.app.sehatin.ui.activities.main.fragments.sendChat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.R
import com.app.sehatin.databinding.FragmentSendChatBinding

class SendChatFragment : Fragment() {
    private lateinit var binding: FragmentSendChatBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSendChatBinding.inflate(inflater, container, false)
        return binding.root
    }

}