package com.app.sehatin.ui.activities.objectDetection

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.databinding.ActivityObjectDetectionBinding
import com.app.sehatin.utils.FileHelper
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import java.io.File

class ObjectDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityObjectDetectionBinding
    private lateinit var viewModel: ObjectDetectionViewModel
    private var selectedImageFile: File? = null
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetectionBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this@ObjectDetectionActivity)[ObjectDetectionViewModel::class.java]
        initListener()
        setContentView(binding.root)
        startIntentCamera()
    }

    private fun initListener() = with(binding) {
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.cameraBtn -> {
                    startIntentCamera()
                    return@setOnMenuItemClickListener  true
                }
            }
            false
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setViewAndDetect(bitmap: Bitmap) = with(binding) {
        imageView.setImageBitmap(bitmap)
        lifecycleScope.launch {
            runObjectDetection(bitmap)
        }
    }

    private fun runObjectDetection(bitmap: Bitmap) {
        val image = TensorImage.fromBitmap(bitmap)
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setMaxResults(5)
            .setScoreThreshold(0.2f)
            .build()
        val detector = ObjectDetector.createFromFileAndOptions(this, MODEL_FILE_PATH, options)
        val results = detector.detect(image)
        if(results.isNotEmpty()) {
            Log.d(TAG, "runObjectDetection: has result")
            viewModel.detectorResults = results
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
        } else {
            binding.noResultInfo.visibility = View.VISIBLE
            Log.d(TAG, "runObjectDetection: no result")
        }
    }

    private fun setRvResult(results: List<Detection>) = with(binding) {
        resultText.visibility = View.VISIBLE
        rvResult.visibility = View.VISIBLE
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

    private fun startIntentCamera() {
        if (!allCameraPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS_CAMERA, REQUEST_CODE_CAMERA)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)
            FileHelper.createTempFile(this).also {
                val photoURI: Uri = FileProvider.getUriForFile(this, AUTHOR, it)
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private fun allCameraPermissionsGranted() = REQUIRED_PERMISSIONS_CAMERA.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            selectedImageFile = FileHelper.reduceFileImage(File(currentPhotoPath))
            if(selectedImageFile != null) {
                val result =  BitmapFactory.decodeFile(selectedImageFile!!.path)
                setViewAndDetect(result)
                Log.d(TAG, "result: $result")
            } else {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startIntentCamera()
                } else {
                    Toast.makeText(this, "Akses ditolak", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val MAX_FONT_SIZE = 96F
        private const val TAG = "ObjectDetectionActivity"
        private const val MODEL_FILE_PATH = "sehatin_modelV3.tflite"
        val REQUIRED_PERMISSIONS_CAMERA = arrayOf(Manifest.permission.CAMERA)
        const val REQUEST_CODE_CAMERA = 10
        const val AUTHOR = "com.app.sehatin"
    }

}