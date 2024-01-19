package com.maou.themeal.presentation.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maou.themeal.databinding.ItemMealsBinding
import com.maou.themeal.model.MealRecipe
import com.maou.themeal.presentation.detail.MealDetailActivity
import com.maou.themeal.presentation.search.SearchAdapter

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val listMeals = arrayListOf<MealRecipe>()

    fun setListMeals(list: List<MealRecipe>) {
        listMeals.clear()
        listMeals.addAll(list)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemMealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMeals[position])
    }

    override fun getItemCount(): Int {
        return listMeals.size
    }

    inner class ViewHolder(private val itemBinding: ItemMealsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(recipe: MealRecipe) {
            with(itemBinding) {
                tvMeal.text = recipe.meal
                Glide.with(itemView.context).load(recipe.mealThumb).into(imgThumb)
            }

            itemView.setOnClickListener {
                Intent(itemView.context, MealDetailActivity::class.java).also {
                    it.putExtra(MealDetailActivity.MEAL_ID, recipe.id)
                    itemView.context.startActivity(it)
                }
            }
        }
    }
}