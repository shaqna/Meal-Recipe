package com.maou.themeal.presentation.detail

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maou.themeal.data.repository.MealRepository
import com.maou.themeal.model.MealRecipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModelOf

class MealDetailViewModel(
    private val mealsRepository: MealRepository
) : ViewModel() {

    private val _detailState: MutableStateFlow<DetailUiState> = MutableStateFlow(DetailUiState.Init)
    val detailState: StateFlow<DetailUiState> = _detailState

    private var mealId: String = ""

    fun processIntent(intent: Intent) {
        mealId = intent.getStringExtra(MealDetailActivity.MEAL_ID).toString()
    }

    fun fetchRecipe() {
        viewModelScope.launch {
            mealsRepository.getMealRecipe(mealId)
                .onEach {
                    _detailState.value = DetailUiState.ShowLoading(true)
                }.catch { cause ->
                    _detailState.value = DetailUiState.ShowLoading(false)
                    _detailState.value = DetailUiState.ShowMessage(cause.message.toString())
                }
                .collect { result ->
                    _detailState.value = DetailUiState.ShowLoading(false)

                    result.fold(
                        onSuccess = { recipe ->
                            _detailState.value = DetailUiState.Success(recipe)
                        },
                        onFailure = { cause ->
                            _detailState.value = DetailUiState.ShowMessage(cause.message.toString())
                        }
                    )
                }
        }
    }

    fun setFavorite(recipe: MealRecipe) {
        viewModelScope.launch {
            mealsRepository.addFavoriteRecipe(recipe)
        }
    }

    companion object {
        fun inject() = module {
            viewModelOf(::MealDetailViewModel)
        }
    }
}

sealed interface DetailUiState {
    data object Init : DetailUiState
    data class ShowLoading(val isLoading: Boolean) : DetailUiState
    data class ShowMessage(val message: String) : DetailUiState
    data class Success(val recipe: MealRecipe) : DetailUiState
}