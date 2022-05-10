package com.app.sehatin.data.remote.response

import com.app.sehatin.data.model.Food

data class FoodResponse(
    var error: Boolean,
    var message: String,
    var food: List<Food>
)