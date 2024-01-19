package com.maou.themeal.presentation.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maou.themeal.databinding.ItemCategoriesBinding
import com.maou.themeal.model.Categories
import com.maou.themeal.model.Category
import com.maou.themeal.presentation.meals.MealsActivity

class CategoryMealAdapter : RecyclerView.Adapter<CategoryMealAdapter.CategoryViewHolder>() {

    private val listCategory = arrayListOf<Category>()

    fun setMealCategory(list: List<Category>) {
        listCategory.clear()
        listCategory.addAll(list)

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemBinding =
            ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CategoryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(listCategory[position])
    }

    override fun getItemCount(): Int = listCategory.size

    inner class CategoryViewHolder(private val binding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(mealCategory: Category) {
                with(binding) {
                    tvKategori.text = mealCategory.category
                    Glide.with(itemView.context).load(mealCategory.thumb).into(imgKategori)
                }

                itemView.setOnClickListener {
                    Intent(itemView.context, MealsActivity::class.java).also {
                        it.putExtra(MealsActivity.MEAL_CATEGORY, mealCategory.category)
                        it.putExtra(MealsActivity.CATEGORY_DESCRIPTION, mealCategory.description)
                        it.putExtra(MealsActivity.CATEGORY_IMAGE, mealCategory.thumb)
                        it.putExtra(MealsActivity.FILTER_BY, MealsActivity.MEAL_CATEGORY)
                        itemView.context.startActivity(it)
                    }
                }
            }

    }

}