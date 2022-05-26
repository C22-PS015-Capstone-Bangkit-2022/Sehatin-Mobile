package com.app.sehatin.ui.activities.objectDetection

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.sehatin.databinding.ActivityObjectDetectionBinding

class ObjectDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityObjectDetectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetectionBinding.inflate(layoutInflater)
        initVariable()
        initListener()
        setContentView(binding.root)
    }

    private fun initVariable() = with(binding) {
        imageView.setImageBitmap(IMAGE)
    }

    private fun initListener() {

    }

    companion object {
        var IMAGE: Bitmap? = null
   }

}