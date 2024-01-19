package com.maou.themeal.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maou.themeal.data.repository.MealRepository
import com.maou.themeal.model.MealRecipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModelOf

class SearchViewModel(
    private val mealsRepository: MealRepository
) : ViewModel() {

    private val _searchState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState.Init)
    val searchState: StateFlow<SearchUiState> = _searchState


    fun searchMeal(keyword: String) {
        viewModelScope.launch {
            mealsRepository.searchMealRecipe(keyword)
                .onStart {
                    _searchState.value = SearchUiState.ShowLoading(true)
                }.catch { cause ->
                    _searchState.value = SearchUiState.ShowLoading(false)
                    _searchState.value = SearchUiState.ShowMessage(cause.message.toString())
                }
                .collect { result ->
                    _searchState.value = SearchUiState.ShowLoading(false)
                    result.fold(
                        onSuccess = {
                            _searchState.value = SearchUiState.Success(it)
                        },
                        onFailure = { cause ->
                            _searchState.value = SearchUiState.ShowMessage(cause.message.toString())
                        }
                    )
                }
        }
    }

    companion object {
        fun inject() = module {
            viewModelOf(::SearchViewModel)
        }
    }
}

sealed interface SearchUiState {
    data object Init : SearchUiState
    data class ShowLoading(val isLoading: Boolean) : SearchUiState
    data class ShowMessage(val message: String) : SearchUiState
    data class Success(val mealRecipe: List<MealRecipe>) : SearchUiState

}