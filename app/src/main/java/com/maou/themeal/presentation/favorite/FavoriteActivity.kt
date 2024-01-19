package com.maou.themeal.presentation.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.maou.themeal.R
import com.maou.themeal.databinding.ActivityFavoriteBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.context.loadKoinModules
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : AppCompatActivity() {

    private val binding: ActivityFavoriteBinding by lazy {
        ActivityFavoriteBinding.inflate(layoutInflater)
    }

    private val favAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter()
    }

    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadKoinModules(FavoriteViewModel.inject())

        getFavoriteRecipes()
        observeState()
    }

    private fun observeState() {
        viewModel.favoriteState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                handleState(it)
            }.launchIn(lifecycleScope)
    }

    private fun handleState(it: FavoriteUiState) {
        when(it) {
            FavoriteUiState.Init -> Unit
            is FavoriteUiState.Success -> {
                with(binding) {
                    rvSearchList.apply {
                        adapter = favAdapter
                        layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        setHasFixedSize(true)
                    }
                    Log.d("SearchActivity", it.listRecipes.toString())
                    favAdapter.setListMeals(it.listRecipes)
                }
            }
        }
    }

    private fun getFavoriteRecipes() {
        viewModel.getAllFavorite()
    }


}