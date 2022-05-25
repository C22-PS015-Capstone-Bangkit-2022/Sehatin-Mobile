package com.app.sehatin.ui.activities.objectDetection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.sehatin.databinding.ActivityObjectDetectionBinding

class ObjectDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityObjectDetectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}