package com.maou.themeal.presentation.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.maou.themeal.R
import com.maou.themeal.databinding.ActivitySearchBinding
import com.maou.themeal.presentation.meals.MealsAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class SearchActivity : AppCompatActivity() {

    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val mealAdapter: SearchAdapter by lazy {
        SearchAdapter()
    }

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadKoinModules(SearchViewModel.inject())

        setSearch()
        observeSearchState()
    }

    private fun setSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    viewModel.searchMeal(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return true
            }

        })
    }

    private fun observeSearchState() {
        viewModel.searchState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                handleSearchState(it)
            }.launchIn(lifecycleScope)
    }

    private fun handleSearchState(state: SearchUiState) {
        when(state){
            SearchUiState.Init -> Unit
            is SearchUiState.ShowLoading -> {
                showProgressBar(state.isLoading)
            }
            is SearchUiState.ShowMessage -> {
                Log.d("SearchActivity", state.message)
            }
            is SearchUiState.Success -> {
                with(binding) {
                    rvSearchList.apply {
                        adapter = mealAdapter
                        layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        setHasFixedSize(true)
                    }
                    Log.d("SearchActivity", state.mealRecipe.toString())
                    mealAdapter.setListMeals(state.mealRecipe)
                }
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}