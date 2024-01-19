package com.maou.themeal.presentation.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maou.themeal.databinding.ItemAreasBinding
import com.maou.themeal.model.Area
import com.maou.themeal.presentation.meals.MealsActivity
import com.maou.themeal.presentation.meals.MealsActivity.Companion.AREA

class AreaAdapter: RecyclerView.Adapter<AreaAdapter.ViewHolder>() {

    private val listArea = arrayListOf<Area>()

    fun setListArea(list: List<Area>) {
        listArea.clear()
        listArea.addAll(list)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaAdapter.ViewHolder {
        val itemBinding = ItemAreasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AreaAdapter.ViewHolder, position: Int) {
        holder.binding(listArea[position])
    }

    override fun getItemCount(): Int = listArea.size

    inner class ViewHolder(
        private val binding: ItemAreasBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun binding(area: Area) {
            if(area.area == "American" || area.area == "Canadian") {
                with(binding) {
                    tvArea.text = area.area
                    Glide.with(itemView.context).load(area.image).into(imgArea)
                }

                itemView.setOnClickListener {
                    Intent(itemView.context, MealsActivity::class.java).also {
                        it.putExtra(MealsActivity.AREA, area.area)
                        it.putExtra(MealsActivity.FILTER_BY, AREA)
                        it.putExtra(MealsActivity.CATEGORY_IMAGE, area.image)
                        it.putExtra(MealsActivity.CATEGORY_DESCRIPTION, area.description)
                        itemView.context.startActivity(it)
                    }
                }
            }
        }
    }
}