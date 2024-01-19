package com.maou.themeal.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.maou.themeal.R
import com.maou.themeal.databinding.ActivityMealDetailBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class MealDetailActivity : AppCompatActivity() {

    private val binding: ActivityMealDetailBinding by lazy {
        ActivityMealDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: MealDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadKoinModules(MealDetailViewModel.inject())

        processIntent(intent)
        fetchRecipe()
        observeRecipe()

    }


    private fun processIntent(intent: Intent) {
        viewModel.processIntent(intent)
    }

    private fun fetchRecipe() {
        viewModel.fetchRecipe()
    }

    private fun observeRecipe() {
        viewModel.detailState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                handleRecipeState(it)
            }.launchIn(lifecycleScope)
    }

    private fun handleRecipeState(state: DetailUiState) {
        when (state) {
            DetailUiState.Init -> Unit
            is DetailUiState.ShowLoading -> {}
            is DetailUiState.ShowMessage -> {
                Log.d("MealDetailActivity", state.message)
            }

            is DetailUiState.Success -> {
                with(binding) {
                    val recipe = state.recipe
                    tvTitle.text = recipe.meal
                    tvIngredients.text = recipe.ingredients
                    tvInstructions.text = recipe.instructions
                    tvMeasure.text = recipe.measures
                    tvSubTitle.text = "${recipe.category} | ${recipe.area}"

                    tvSource.setOnClickListener {
                        val intentSource = Intent(Intent.ACTION_VIEW)
                        intentSource.data = Uri.parse(recipe.source)
                        startActivity(intentSource)
                    }

                    tvYoutube.setOnClickListener {
                        val intentYoutube = Intent(Intent.ACTION_VIEW)
                        intentYoutube.data = Uri.parse(recipe.youtube)
                        startActivity(intentYoutube)
                    }

                    tvShareRecipe.setOnClickListener {
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_TEXT, recipe.source);
                        startActivity(Intent.createChooser(shareIntent, "Share with"))
                    }

                    Glide.with(this@MealDetailActivity).load(recipe.mealThumb).into(imgThumb)

                    imgFavorite.setOnClickListener {
                        viewModel.setFavorite(recipe)
                        Toast.makeText(
                            this@MealDetailActivity,
                            "Telah ditambahkan ke favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    companion object {
        const val MEAL_ID = "id"
    }
}