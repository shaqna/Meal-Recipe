package com.maou.themeal.presentation.meals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.maou.themeal.R
import com.maou.themeal.databinding.ActivityMealsBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.context.loadKoinModules
import org.koin.androidx.viewmodel.ext.android.viewModel

class MealsActivity : AppCompatActivity() {

    private val binding: ActivityMealsBinding by lazy {
        ActivityMealsBinding.inflate(layoutInflater)
    }

    private val mealsAdapter: MealsAdapter by lazy {
        MealsAdapter()
    }

    private val viewModel: MealsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadKoinModules(MealsViewModel.inject())

        setBackButton()
        processIntent(intent)

        observeMealsFiltered()
    }

    private fun setBackButton() {
        binding.toolbarFilter.setNavigationOnClickListener {
            finish()
        }
    }

    private fun processIntent(intent: Intent) {
        viewModel.processIntent(intent)
        fetchMeals()
    }

    private fun fetchMeals() {

        if (viewModel.filterBy == MEAL_CATEGORY) {
            viewModel.filterMealsByCategory()
        } else if (viewModel.filterBy == AREA) {
            viewModel.filterMealsByArea()
        }


    }

    private fun observeMealsFiltered() {
        viewModel.mealState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                handleMealsState(it)
            }.launchIn(lifecycleScope)
    }

    private fun handleMealsState(state: MealsUiState) {
        when (state) {
            MealsUiState.Init -> Unit
            is MealsUiState.ShowLoading -> {}
            is MealsUiState.ShowMessage -> {
                Log.d("MealsActivity", state.message)
                Toast.makeText(this@MealsActivity, state.message, Toast.LENGTH_SHORT).show()
            }

            is MealsUiState.Success -> {
                with(binding) {
                    rvFilter.apply {
                        adapter = mealsAdapter
                        layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        setHasFixedSize(true)
                    }

                    if (viewModel.filterBy == MEAL_CATEGORY) {
                        tvTitle.text = viewModel.category
                        tvDescCategories.text = viewModel.description
                        Glide.with(this@MealsActivity).load(viewModel.categoryImage)
                            .into(imgCategories)
                    } else if (viewModel.filterBy == AREA) {
                        tvTitle.text = "${viewModel.area} Food"
                        tvDescCategories.text = viewModel.description
                        Glide.with(this@MealsActivity).load(viewModel.categoryImage)
                            .into(imgCategories)
                    }


                    mealsAdapter.setListMeals(state.meals.mealsFilter)
                }
            }
        }
    }

    companion object {
        const val MEAL_CATEGORY = "MEAL Category"
        const val CATEGORY_DESCRIPTION = "Meal description"
        const val CATEGORY_IMAGE = "category image"
        const val AREA = "area"

        const val FILTER_BY = "filter_by"

    }
}