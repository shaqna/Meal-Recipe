package com.maou.themeal.presentation.meals

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maou.themeal.databinding.ItemMealsBinding
import com.maou.themeal.model.MealInfo
import com.maou.themeal.presentation.detail.MealDetailActivity

class MealsAdapter : RecyclerView.Adapter<MealsAdapter.ViewHolder>() {

    private val listMeals = arrayListOf<MealInfo>()

    fun setListMeals(list: List<MealInfo>) {
        listMeals.clear()
        listMeals.addAll(list)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsAdapter.ViewHolder {
        val itemBinding = ItemMealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MealsAdapter.ViewHolder, position: Int) {
        holder.bind(listMeals[position])
    }

    override fun getItemCount(): Int {
        return listMeals.size
    }

    inner class ViewHolder(private val binding: ItemMealsBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(meal: MealInfo) {
                with(binding) {
                    tvMeal.text = meal.meal
                    Glide.with(itemView.context).load(meal.mealThumb).into(imgThumb)
                }

                itemView.setOnClickListener {
                    Intent(itemView.context, MealDetailActivity::class.java).also {
                        it.putExtra(MealDetailActivity.MEAL_ID, meal.id)
                        itemView.context.startActivity(it)
                    }
                }
            }
        }
}