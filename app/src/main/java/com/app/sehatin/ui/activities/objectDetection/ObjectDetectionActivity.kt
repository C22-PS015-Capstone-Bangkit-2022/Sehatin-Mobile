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
import androidx.recyclerview.widget.GridLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Food
import com.app.sehatin.databinding.ActivityObjectDetectionBinding
import com.app.sehatin.ui.activities.main.fragments.content.fragments.health.fragments.food.FoodAdapter
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.app.sehatin.utils.FileHelper
import org.tensorflow.lite.task.vision.detector.Detection
import java.io.File

class ObjectDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityObjectDetectionBinding
    private lateinit var viewModel: ObjectDetectionViewModel
    private var selectedImageFile: File? = null
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetectionBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this@ObjectDetectionActivity, ViewModelFactory.getInstance())[ObjectDetectionViewModel::class.java]
        initListener()
        setContentView(binding.root)
        startIntentCamera()
    }

    private fun initListener() = with(binding) {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.cameraBtn -> {
                    startIntentCamera()
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun detect(bitmap: Bitmap) = viewModel.detectImage(bitmap, this).observe(this) {
        when (it) {
            is Result.Loading -> {
                showLoading(true)
                Log.d(TAG, "detect: loading")
            }
            is Result.Error -> {
                showLoading(false)
                Log.e(TAG, "detect: error = ${it.error}")
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
            }
            is Result.Success -> {
                showLoading(false)
                Log.d(TAG, "detect: success = ${it.data}")
                val results = it.data
                onSuccessDetect(bitmap, results)
            }
        }
    }

    private fun onSuccessDetect(bitmap: Bitmap, results: List<Detection>) = with(binding) {
        if (results.isNotEmpty()) {
            viewModel.detectorResults = results
            val resultToDisplay = viewModel.detectorResults.map {
                val category = it.categories.first()
                val text = "${category.label}, ${category.score.times(100).toInt()}%"
                DetectionResult(it.boundingBox, text)
            }
            noResultInfo.visibility = View.GONE
            viewModel.imageWithResult = drawDetectionResult(bitmap, resultToDisplay)
            imageView.setImageBitmap(viewModel.imageWithResult)
            findFoods(viewModel.detectorResults)
        } else {
            rvFoods.visibility = View.GONE
            infoGizi.visibility = View.GONE
            noResultInfo.visibility = View.VISIBLE
        }
    }

    private fun findFoods(detectorResults: List<Detection>) {
        val foodNames = mutableListOf<String>()
        detectorResults.forEach {
            foodNames.add(it.categories.first().label)
        }
        viewModel.findFoods(foodNames).observe(this) {
            when (it) {
                is Result.Loading -> {
                    showFindFoodsLoading(true)
                }
                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "findFoods: error = ${it.error}")
                }   
                is Result.Success -> {
                    showFindFoodsLoading(false)
                    it.data?.food?.let { foods ->
                        calculateNutrition(foods)
                        setRvFoods(foods)
                    }
                }
            }
        }
    }

    private fun setRvFoods(food: List<Food>) = with(binding) {
        var spanCount = 1
        if(food.size > 1) {
            spanCount = 2
        }
        rvFoods.setHasFixedSize(true)
        rvFoods.layoutManager = GridLayoutManager(this@ObjectDetectionActivity, spanCount)
        rvFoods.adapter = FoodAdapter(food)
    }

    private fun calculateNutrition(foods: List<Food>) = with(binding) {
        var protein = 0.0
        var fat = 0.0
        var karbo = 0.0
        var kalori = 0.0
        for(food in foods) {
            food.protein?.let {
                protein += it
            }
            food.fat?.let {
                fat += it
            }
            food.carbs?.let {
                karbo += it
            }
            food.energy?.let {
                kalori += it
            }
        }
        proteinText.text = protein.toString()
        fatText.text = fat.toString()
        karboText.text = karbo.toString()
        kaloriText.text = kalori.toString()
    }

    private fun showFindFoodsLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            infoGizi.visibility = View.GONE
            rvFoods.visibility = View.GONE
            shimmerLoading.root.visibility = View.VISIBLE
        } else {
            infoGizi.visibility = View.VISIBLE
            rvFoods.visibility = View.VISIBLE
            shimmerLoading.root.visibility = View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            contentLayout.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            contentLayout.visibility = View.VISIBLE
        }
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
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS_CAMERA,
                REQUEST_CODE_CAMERA
            )
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
            if (selectedImageFile != null) {
                val result = BitmapFactory.decodeFile(selectedImageFile!!.path)
                binding.imageView.setImageBitmap(result)
                detect(result)
            } else {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
        private const val MAX_FONT_SIZE = 50F
        private const val TAG = "ObjectDetectionActivity"
        val REQUIRED_PERMISSIONS_CAMERA = arrayOf(Manifest.permission.CAMERA)
        const val REQUEST_CODE_CAMERA = 10
        const val AUTHOR = "com.app.sehatin"
    }

}