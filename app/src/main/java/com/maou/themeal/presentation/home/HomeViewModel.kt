package com.maou.themeal.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maou.themeal.data.repository.MealRepository
import com.maou.themeal.model.Areas
import com.maou.themeal.model.Categories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModelOf

class HomeViewModel(
    private val mealRepository: MealRepository
) : ViewModel() {



    private val _categoryState: MutableStateFlow<CategoryUiState> =
        MutableStateFlow(CategoryUiState.Init)
    val categoryState: StateFlow<CategoryUiState> = _categoryState

    private val _areaState: MutableStateFlow<AreaUiState> = MutableStateFlow(AreaUiState.Init)
    val areaState: StateFlow<AreaUiState> = _areaState

    fun getCategories() {
        viewModelScope.launch {
            mealRepository.getCategories()
                .onStart {
                    _categoryState.value = CategoryUiState.ShowLoading(true)
                }.catch { cause ->
                    Log.d("ViewModel: Category: ", cause.message.toString())
                    _categoryState.value = CategoryUiState.ShowMessage(cause.message.toString())
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { categories ->
                            Log.d("ViewModel: Category: ", categories.toString())
                            _categoryState.value = CategoryUiState.Success(categories)
                        },
                        onFailure = { cause ->
                            Log.d("ViewModel: Category: ", cause.message.toString())
                            _categoryState.value =
                                CategoryUiState.ShowMessage(cause.message.toString())
                        }
                    )
                }
        }
    }

    fun getAreas() {
        viewModelScope.launch {
            mealRepository.getAreas()
                .onStart {
                    _areaState.value = AreaUiState.ShowLoading(true)
                }.catch { cause ->
                    Log.e("ViewModel: Area: ", cause.message.toString())
                    _areaState.value = AreaUiState.ShowMessage(cause.message.toString())
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { categories ->
                            Log.d("ViewModel: Area: ", categories.toString())
                            _areaState.value = AreaUiState.Success(categories)
                        },
                        onFailure = { cause ->
                            Log.e("ViewModel: Category: ", cause.message.toString())
                            _areaState.value =
                                AreaUiState.ShowMessage(cause.message.toString())
                        }
                    )
                }
        }
    }

    fun getRecipe() {
        viewModelScope.launch {
            mealRepository.getMealRecipe("52924").
                    collect {
                        it.fold(
                            onSuccess = {recipe ->
                                Log.d("ViewModel: Food: ", recipe.toString())
                            },
                            onFailure = {cause ->
                                Log.e("ViewModel: Food: ", cause.message.toString())
                            }
                        )
                    }
        }
    }


    companion object {
        fun inject() = module {
            viewModelOf(::HomeViewModel)
        }
    }
}

sealed interface CategoryUiState {
    data object Init : CategoryUiState
    data class Success(val categories: Categories) : CategoryUiState
    data class ShowLoading(val isLoading: Boolean) : CategoryUiState
    data class ShowMessage(val message: String) : CategoryUiState
}

sealed interface AreaUiState {
    data object Init : AreaUiState
    data class Success(val areas: Areas) : AreaUiState
    data class ShowLoading(val isLoading: Boolean) : AreaUiState
    data class ShowMessage(val message: String) : AreaUiState
}