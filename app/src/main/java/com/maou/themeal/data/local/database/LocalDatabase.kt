package com.maou.themeal.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maou.themeal.data.local.dao.RecipeDao
import com.maou.themeal.data.local.entity.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}