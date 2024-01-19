package com.maou.themeal.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maou.themeal.data.repository.MealRepository
import com.maou.themeal.model.MealInfo
import com.maou.themeal.model.MealRecipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModelOf

class FavoriteViewModel(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _favoriteState: MutableStateFlow<FavoriteUiState> =
        MutableStateFlow(FavoriteUiState.Init)
    val favoriteState: StateFlow<FavoriteUiState> = _favoriteState

    fun deleteFavorite(recipe: MealRecipe) {
        viewModelScope.launch {
            mealRepository.addFavoriteRecipe(recipe)
        }
    }

    fun getAllFavorite(){
        viewModelScope.launch {
            mealRepository.loadAllFavoriteRecipe().collect {
                _favoriteState.value = FavoriteUiState.Success(it)
            }
        }
    }

    companion object {
        fun inject() = module {
            viewModelOf(::FavoriteViewModel)
        }
    }

}

sealed interface FavoriteUiState {
    data object Init : FavoriteUiState
    data class Success(val listRecipes: List<MealRecipe>) : FavoriteUiState
}