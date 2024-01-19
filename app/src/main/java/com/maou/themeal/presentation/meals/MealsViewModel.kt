package com.maou.themeal.presentation.meals

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maou.themeal.data.repository.MealRepository
import com.maou.themeal.model.MealFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModelOf

class MealsViewModel(
    private val mealsRepository: MealRepository
) : ViewModel() {
    private val _mealsUiState: MutableStateFlow<MealsUiState> = MutableStateFlow(MealsUiState.Init)
    val mealState: StateFlow<MealsUiState> = _mealsUiState

    var category: String = ""
    var area: String = ""
    var description: String = ""
    var categoryImage: String = ""
    var filterBy: String = ""

    fun processIntent(intent: Intent) {

        filterBy = intent.getStringExtra(MealsActivity.FILTER_BY)!!

        category = intent.getStringExtra(MealsActivity.MEAL_CATEGORY).toString()
        description = intent.getStringExtra(MealsActivity.CATEGORY_DESCRIPTION).toString()
        categoryImage = intent.getStringExtra(MealsActivity.CATEGORY_IMAGE).toString()
        area = intent.getStringExtra(MealsActivity.AREA).toString()
    }

    fun filterMealsByCategory() {
        Log.d("ViewModel", "Category")
        viewModelScope.launch {
            mealsRepository.getMealsByCategory(category!!)
                .onStart {
                    _mealsUiState.value = MealsUiState.ShowLoading(true)
                }.catch { cause ->
                    _mealsUiState.value = MealsUiState.ShowLoading(false)
                    _mealsUiState.value = MealsUiState.ShowMessage(cause.message.toString())
                }.collect { result ->
                    _mealsUiState.value = MealsUiState.ShowLoading(false)

                    result.fold(
                        onSuccess = { meals ->
                            _mealsUiState.value = MealsUiState.Success(meals)
                        },
                        onFailure = { cause ->
                            _mealsUiState.value = MealsUiState.ShowMessage(cause.message.toString())
                        }
                    )
                }
        }
    }

    fun filterMealsByArea() {
        Log.d("ViewModel", "Area")
        viewModelScope.launch {
            mealsRepository.getMealsByArea(area!!)
                .onStart {
                    _mealsUiState.value = MealsUiState.ShowLoading(true)
                }.catch { cause ->
                    _mealsUiState.value = MealsUiState.ShowLoading(false)
                    _mealsUiState.value = MealsUiState.ShowMessage(cause.message.toString())
                }.collect { result ->
                    _mealsUiState.value = MealsUiState.ShowLoading(false)

                    result.fold(
                        onSuccess = { meals ->
                            _mealsUiState.value = MealsUiState.Success(meals)
                        },
                        onFailure = { cause ->
                            _mealsUiState.value = MealsUiState.ShowMessage(cause.message.toString())
                        }
                    )
                }
        }
    }


    companion object {
        fun inject() = module {
            viewModelOf(::MealsViewModel)
        }
    }


}

sealed interface MealsUiState {
    data object Init : MealsUiState
    data class ShowLoading(val isLoading: Boolean) : MealsUiState
    data class ShowMessage(val message: String) : MealsUiState
    data class Success(val meals: MealFilter): MealsUiState
}