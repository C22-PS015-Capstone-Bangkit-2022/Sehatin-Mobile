package com.app.sehatin.ui.activities.objectDetection

import android.graphics.RectF

data class DetectionResult(
    val boundingBox: RectF,
    val text: String
)