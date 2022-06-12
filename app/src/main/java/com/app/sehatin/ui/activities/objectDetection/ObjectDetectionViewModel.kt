package com.app.sehatin.ui.activities.objectDetection

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.repository.FoodRepository
import com.app.sehatin.data.repository.ObjectDetectionRepository
import org.tensorflow.lite.task.vision.detector.Detection

class ObjectDetectionViewModel(
    private val objectDetectionRepository: ObjectDetectionRepository,
    private val foodRepository: FoodRepository,
    ): ViewModel() {

    var imageWithResult: Bitmap? = null
    var detectorResults = listOf<Detection>()

    fun detectImage(bitmap: Bitmap, context: Context) = objectDetectionRepository.detectImage(bitmap, context)

    fun findFoods(foodNames: List<String>) = foodRepository.findFoods(foodNames)

}