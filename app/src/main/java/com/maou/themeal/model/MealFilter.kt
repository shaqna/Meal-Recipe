package com.maou.themeal.model


data class MealFilter(
    val mealsFilter: List<MealInfo>
)

data class MealInfo(
    val meal: String,
    val mealThumb: String,
    val id: String
)
