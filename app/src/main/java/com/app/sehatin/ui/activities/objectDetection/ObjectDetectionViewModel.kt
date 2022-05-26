package com.app.sehatin.ui.activities.objectDetection

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import org.tensorflow.lite.task.vision.detector.Detection

class ObjectDetectionViewModel: ViewModel() {

    var imageWithResult: Bitmap? = null
    var detectorResults = mutableListOf<Detection>()

}