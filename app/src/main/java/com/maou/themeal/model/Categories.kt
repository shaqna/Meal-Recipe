package com.maou.themeal.model

import com.squareup.moshi.Json

data class Categories(
    val categories: List<Category>
)

data class Category(
    val category: String,
    val id: String,
    val thumb: String,
    val description: String
)
