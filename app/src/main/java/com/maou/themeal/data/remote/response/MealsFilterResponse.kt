package com.maou.themeal.data.remote.response

import com.squareup.moshi.Json

data class MealsFilterResponse(
    @field:Json(name = "meals")
    val meals: List<MealsInfoResponse>
)

data class MealsInfoResponse(
    @field:Json(name = "strMeal")
    val strMeal: String,
    @field:Json(name = "strMealThumb")
    val strMealThumb: String,
    @field:Json(name = "idMeal")
    val idMeal: String
)
