package com.maou.themeal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey
    val id: String,
    val meal: String,
    val category: String,
    val area: String,
    val instructions: String,
    val mealThumb: String,
    val tags: String,
    val youtube: String,
    val source: String,
    val ingredients: String,
    val measures: String,
    val isFavorite: Boolean
)
