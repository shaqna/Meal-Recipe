package com.maou.themeal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maou.themeal.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("select * from recipe")
    fun getAllFavoriteRecipe(): Flow<List<RecipeEntity>>

}