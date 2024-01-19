package com.maou.themeal.data.repository

import com.maou.themeal.data.local.dao.RecipeDao
import com.maou.themeal.data.local.entity.RecipeEntity
import com.maou.themeal.data.mapper.toFlowListModel
import com.maou.themeal.data.mapper.toListModel
import com.maou.themeal.data.mapper.toModel
import com.maou.themeal.data.remote.service.ApiService
import com.maou.themeal.model.Areas
import com.maou.themeal.model.Categories
import com.maou.themeal.model.MealFilter
import com.maou.themeal.model.MealInfo
import com.maou.themeal.model.MealRecipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MealRepository(
    private val apiService: ApiService,
    private val recipeDao: RecipeDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun getCategories(): Flow<Result<Categories>> =
        flow {
            try {
                val response = apiService.getCategoryList()
                emit(Result.success(response.toModel()))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }

        }.catch { cause ->
            emit(Result.failure(cause))
        }.flowOn(Dispatchers.IO)


    fun getAreas(): Flow<Result<Areas>> =
        flow {
            try {
                val response = apiService.getAreaList()
                emit(Result.success(response.toModel()))
            } catch (e: Exception){
                emit(Result.failure(e))
            }
        }.catch {cause ->
            emit(Result.failure(cause))
        }.flowOn(Dispatchers.IO)

    fun getMealsByCategory(category: String) : Flow<Result<MealFilter>> =
        flow {
            try {
                val response = apiService.getMealsByCategory(category)
                emit(Result.success(response.toModel()))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.catch {cause ->
            emit(Result.failure(cause))
        }.flowOn(Dispatchers.IO)

    fun getMealsByArea(area: String) : Flow<Result<MealFilter>> =
        flow {
            try {
                val response = apiService.getMealsByArea(area)
                emit(Result.success(response.toModel()))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.catch {cause ->
            emit(Result.failure(cause))
        }.flowOn(Dispatchers.IO)

    fun getMealRecipe(id: String): Flow<Result<MealRecipe>> =
        flow {
            try {
                val response = apiService.getMealRecipe(id)
                emit(Result.success(response.toModel()))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.catch { cause ->
            emit(Result.failure(cause))
        }.flowOn(Dispatchers.IO)

    fun searchMealRecipe(keyword: String): Flow<Result<List<MealRecipe>>> =
        flow {
            try {
                val response = apiService.searchRecipeByName(keyword)
                emit(Result.success(response.toListModel()))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.catch { cause ->
            emit(Result.failure(cause))
        }.flowOn(Dispatchers.IO)

    suspend fun addFavoriteRecipe(recipe: MealRecipe) {
        withContext(ioDispatcher) {
            recipeDao.insertRecipe(
                RecipeEntity(
                    id = recipe.id,
                    meal = recipe.meal,
                    mealThumb = recipe.mealThumb,
                    area = recipe.area,
                    instructions = recipe.instructions,
                    ingredients = recipe.ingredients,
                    isFavorite = recipe.isFavorite,
                    youtube = recipe.youtube,
                    source = recipe.source,
                    tags = recipe.tags,
                    measures = recipe.measures,
                    category = recipe.category
                ))
        }
    }

    fun loadAllFavoriteRecipe(): Flow<List<MealRecipe>> {
        val entity = recipeDao.getAllFavoriteRecipe()
        return entity.toFlowListModel()
    }

}