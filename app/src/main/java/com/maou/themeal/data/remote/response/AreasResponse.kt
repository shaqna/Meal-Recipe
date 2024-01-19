package com.maou.themeal.data.remote.response

import com.squareup.moshi.Json

data class AreasResponse(
    @field:Json(name = "meals")
    val meals: List<AreaResponse>
)

data class AreaResponse(
    @field:Json(name = "strArea")
    val strArea: String
)
