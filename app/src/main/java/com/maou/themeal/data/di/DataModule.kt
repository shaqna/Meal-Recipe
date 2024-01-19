package com.maou.themeal.data.di

import androidx.room.Room
import com.maou.themeal.BuildConfig
import com.maou.themeal.data.local.database.LocalDatabase
import com.maou.themeal.data.remote.service.ApiService
import com.maou.themeal.data.repository.MealRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val dataModule = module {
    singleOf(::MealRepository)

    single {
        val retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            ).build()

        retrofit.create(ApiService::class.java)
    }

}

val roomModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            LocalDatabase::class.java,
            "Recipe.db"
        ).fallbackToDestructiveMigration().build()
    }

    factory {
        get<LocalDatabase>().recipeDao()
    }

    single {
        Dispatchers.IO
    }
}