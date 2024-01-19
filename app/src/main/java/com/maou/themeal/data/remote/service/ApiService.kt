package com.maou.themeal.data.remote.service

import com.maou.themeal.data.remote.response.AreasResponse
import com.maou.themeal.data.remote.response.CategoriesResponse
import com.maou.themeal.data.remote.response.MealRecipeResponse
import com.maou.themeal.data.remote.response.MealsFilterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/api/json/v1/1/categories.php")
    suspend fun getCategoryList(): CategoriesResponse

    @GET("/api/json/v1/1/list.php?a=list")
    suspend fun getAreaList(): AreasResponse

    @GET("/api/json/v1/1/filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ): MealsFilterResponse

    @GET("/api/json/v1/1/filter.php")
    suspend fun getMealsByArea(
        @Query("a") area: String
    ): MealsFilterResponse

    @GET("/api/json/v1/1/lookup.php")
    suspend fun getMealRecipe(
        @Query("i") mealId: String
    ): MealRecipeResponse

    @GET("/api/json/v1/1/search.php")
    suspend fun searchRecipeByName(
        @Query("s") keyword: String
    ):MealRecipeResponse
}