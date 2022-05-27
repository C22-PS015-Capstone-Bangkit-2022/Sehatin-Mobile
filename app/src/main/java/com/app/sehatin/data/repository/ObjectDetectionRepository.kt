package com.app.sehatin.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.Result
import kotlinx.coroutines.Dispatchers
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class ObjectDetectionRepository {
    private val modelFilePath = "sehatin_modelV3.tflite"

    fun detectImage(bitmap: Bitmap, context: Context): LiveData<Result<List<Detection>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val image = TensorImage.fromBitmap(bitmap)
            val options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(5)
                .setScoreThreshold(0.2f)
                .build()
            val detector = ObjectDetector.createFromFileAndOptions(context, modelFilePath, options)
            val results = detector.detect(image)
            emitSource(MutableLiveData(Result.Success(results)))
        }
        catch (e: Exception) {
            Log.e(TAG, "detectImage: $e")
            emit(Result.Error(e.toString()))
        }
    }

    private companion object {
        const val TAG = "ObjectDetectionRepository"
    }

}