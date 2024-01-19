package com.maou.themeal.presentation.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maou.themeal.R
import com.maou.themeal.databinding.ActivityHomeBinding
import com.maou.themeal.databinding.ToolbarMainBinding
import com.maou.themeal.presentation.favorite.FavoriteActivity
import com.maou.themeal.presentation.search.SearchActivity
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class HomeActivity : AppCompatActivity() {

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    private val toolbarBinding: ToolbarMainBinding by lazy {
        ToolbarMainBinding.bind(binding.appbar.getChildAt(0))
    }

    private val categoryAdapter: CategoryMealAdapter by lazy {
        CategoryMealAdapter()
    }

    private val areaAdapter: AreaAdapter by lazy {
        AreaAdapter()
    }

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadKoinModules(HomeViewModel.inject())

        setButton()

        fetchCategories()
        observeCategories()
        fetchAreas()
        observeAreas()
    }

    private fun setButton() {
        binding.searchLayout.setOnClickListener {
            Intent(this,SearchActivity::class.java).also {
                startActivity(it)
            }
        }

        toolbarBinding.imgOpenFavorite.setOnClickListener {
            Intent(this,FavoriteActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun fetchCategories() {
        viewModel.getCategories()
    }

    private fun observeCategories() {
        viewModel.categoryState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                handleCategoryState(it)
            }.launchIn(lifecycleScope)
    }

    private fun handleCategoryState(state: CategoryUiState) {
        when (state) {
            CategoryUiState.Init -> Unit
            is CategoryUiState.ShowLoading -> {

            }

            is CategoryUiState.ShowMessage -> {
                Toast.makeText(this@HomeActivity, state.message, Toast.LENGTH_SHORT).show()
            }

            is CategoryUiState.Success -> {
                with(binding) {
                    rvMainMenu.apply {
                        adapter = categoryAdapter
                        layoutManager =
                            GridLayoutManager(this@HomeActivity, 2, RecyclerView.VERTICAL, false)
                        setHasFixedSize(true)
                    }

                    categoryAdapter.setMealCategory(state.categories.categories)
                }
            }
        }
    }

    private fun fetchAreas() {
        viewModel.getAreas()
    }

    private fun observeAreas() {
        viewModel.areaState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                handleAreaState(it)
            }.launchIn(lifecycleScope)
    }

    private fun handleAreaState(state: AreaUiState) {
        when(state) {
            AreaUiState.Init -> Unit
            is AreaUiState.ShowLoading -> {

            }
            is AreaUiState.ShowMessage -> {
                Toast.makeText(this@HomeActivity, state.message, Toast.LENGTH_SHORT).show()
            }
            is AreaUiState.Success -> {
                with(binding) {
                    rvMenuArea.apply {
                        adapter = areaAdapter
                        layoutManager =
                            GridLayoutManager(this@HomeActivity, 2, RecyclerView.VERTICAL, false)
                        setHasFixedSize(true)
                    }

                    areaAdapter.setListArea(state.areas.areas.filter {
                        it.area == "American" || it.area =="Canadian"
                    })
                }
            }
        }
    }

}