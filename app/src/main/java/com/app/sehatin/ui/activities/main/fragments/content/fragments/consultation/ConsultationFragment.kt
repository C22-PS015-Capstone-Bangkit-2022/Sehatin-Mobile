package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.databinding.FragmentConsultationBinding

class ConsultationFragment : Fragment() {
    private lateinit var binding: FragmentConsultationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConsultationBinding.inflate(inflater, container, false)
        return binding.root
    }

}