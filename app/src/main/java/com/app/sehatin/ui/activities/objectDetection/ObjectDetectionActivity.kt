package com.app.sehatin.ui.activities.objectDetection

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.databinding.ActivityObjectDetectionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class ObjectDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityObjectDetectionBinding
    private lateinit var viewModel: ObjectDetectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetectionBinding.inflate(layoutInflater)
        initVariable()
        initListener()
        setContentView(binding.root)
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@ObjectDetectionActivity)[ObjectDetectionViewModel::class.java]
        val image = IMAGE
        if (image != null) {
            setViewAndDetect(image)
        } else {
            Toast.makeText(this@ObjectDetectionActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun initListener() = with(binding) {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setViewAndDetect(bitmap: Bitmap) = with(binding) {
        imageView.setImageBitmap(bitmap)
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                runObjectDetection(bitmap)
            }
        }
    }

    private fun runObjectDetection(bitmap: Bitmap) {
        val image = TensorImage.fromBitmap(bitmap)
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setMaxResults(5)
            .setScoreThreshold(0.2f)
            .build()
        val detector = ObjectDetector.createFromFileAndOptions(this, MODEL_FILE_PATH, options)
        viewModel.detectorResults = detector.detect(image)
        setRvResult(viewModel.detectorResults)
        val resultToDisplay = viewModel.detectorResults.map {
            // Get the top-1 category and craft the display text
            val category = it.categories.first()
            val text = "${category.label}, ${category.score.times(100).toInt()}%"
            // Create a data object to display the detection result
            DetectionResult(it.boundingBox, text)
        }
        // Draw the detection result on the bitmap and show it.
        viewModel.imageWithResult = drawDetectionResult(bitmap, resultToDisplay)
        runOnUiThread {
            binding.imageView.setImageBitmap(viewModel.imageWithResult)
        }
    }

    private fun setRvResult(results: List<Detection>) = with(binding) {
        rvResult.setHasFixedSize(true)
        rvResult.layoutManager = LinearLayoutManager(this@ObjectDetectionActivity)
        rvResult.adapter = DetectorResultAdapter(results)
    }

    private fun drawDetectionResult(bitmap: Bitmap, detectionResults: List<DetectionResult>): Bitmap {
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outputBitmap)
        val pen = Paint()
        pen.textAlign = Paint.Align.LEFT

        detectionResults.forEach {
            // draw bounding box
            pen.color = Color.RED
            pen.strokeWidth = 8F
            pen.style = Paint.Style.STROKE
            val box = it.boundingBox
            canvas.drawRect(box, pen)
            val tagSize = Rect(0, 0, 0, 0)
            // calculate the right font size
            pen.style = Paint.Style.FILL_AND_STROKE
            pen.color = Color.YELLOW
            pen.strokeWidth = 2F
            pen.textSize = MAX_FONT_SIZE
            pen.getTextBounds(it.text, 0, it.text.length, tagSize)
            val fontSize: Float = pen.textSize * box.width() / tagSize.width()
            // adjust the font size so texts are inside the bounding box
            if (fontSize < pen.textSize) pen.textSize = fontSize
            var margin = (box.width() - tagSize.width()) / 2.0F
            if (margin < 0F) margin = 0F
            canvas.drawText(
                it.text, box.left + margin,
                box.top + tagSize.height().times(1F),
                pen
            )
        }
        return outputBitmap
    }

    companion object {
        var IMAGE: Bitmap? = null
        private const val MAX_FONT_SIZE = 96F
        private const val TAG = "ObjectDetectionActivity"
        private const val MODEL_FILE_PATH = "sehatin_modelV3.tflite"
    }

}