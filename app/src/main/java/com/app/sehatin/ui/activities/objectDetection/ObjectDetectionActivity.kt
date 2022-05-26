package com.app.sehatin.ui.activities.objectDetection

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.app.sehatin.R
import com.app.sehatin.databinding.ActivityObjectDetectionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val image = IMAGE
        if(image != null) {
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
        lifecycleScope.launch(Dispatchers.Default) { runObjectDetection(bitmap) }
    }

    private fun runObjectDetection(bitmap: Bitmap) {

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
                box.top + tagSize.height().times(1F), pen
            )
        }
        return outputBitmap
    }

    companion object {
        var IMAGE: Bitmap? = null
        private const val MAX_FONT_SIZE = 96F
   }

}

data class DetectionResult(val boundingBox: RectF, val text: String)