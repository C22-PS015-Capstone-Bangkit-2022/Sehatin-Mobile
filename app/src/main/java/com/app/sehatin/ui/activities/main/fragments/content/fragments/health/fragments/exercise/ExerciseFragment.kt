package com.app.sehatin.ui.activities.main.fragments.content.fragments.health.fragments.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.databinding.FragmentExerciseBinding

class ExerciseFragment : Fragment() {
    private lateinit var binding: FragmentExerciseBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

}