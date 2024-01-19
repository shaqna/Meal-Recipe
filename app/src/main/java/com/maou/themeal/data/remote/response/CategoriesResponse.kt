package com.maou.themeal.data.remote.response

import com.squareup.moshi.Json

data class CategoriesResponse(
    @field:Json(name = "categories")
    val categories: List<CategoryResponse>
)

data class CategoryResponse(
    @field:Json(name = "strCategory")
    val strCategory: String,

    @field:Json(name = "idCategory")
    val idCategory: String,

    @field:Json(name = "strCategoryThumb")
    val strCategoryThumb: String,

    @field:Json(name = "strCategoryDescription")
    val strCategoryDescription: String
)
