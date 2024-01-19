package com.maou.themeal.model

data class Areas(
    val areas: List<Area>
)

data class Area(
    val area: String,
    val image: String,
    val description: String
)
