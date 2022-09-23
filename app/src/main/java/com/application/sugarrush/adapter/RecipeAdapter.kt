package com.application.sugarrush.adapter


import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.RecyclerView
import com.application.sugarrush.databinding.RecipeLayoutFileBinding
import com.application.sugarrush.models.FoodRecipe
import com.application.sugarrush.models.Result
import com.application.sugarrush.util.RecipeDiffUtil

class RecipeAdapter:RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    private var recipes =  emptyList<Result>()

    class RecipeViewHolder(private val binding: RecipeLayoutFileBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(result: Result){
            binding.result = result
            binding.executePendingBindings()
        }
        companion object{
            fun from( parent:ViewGroup):RecipeViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipeLayoutFileBinding.inflate(layoutInflater,parent,false)
                return RecipeViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData( newData:FoodRecipe ){
        val recipeDiffUtil = RecipeDiffUtil(recipes,newData.recipes)
        val diffUtilResult = DiffUtil.calculateDiff(recipeDiffUtil)
        recipes = newData.recipes
        diffUtilResult.dispatchUpdatesTo(this)
    }
}