package com.application.sugarrush.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.application.sugarrush.R
import com.application.sugarrush.databinding.IngredientsRowLayoutBinding
import com.application.sugarrush.models.ExtendedIngredient
import com.application.sugarrush.util.RecipeDiffUtil
import com.application.sugarrush.util.constants.Companion.BASE_IMG_URL
import java.util.*

class IngredientsAdapter :RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>(){
    private var ingredientsList =  emptyList<ExtendedIngredient>()
    class IngredientsViewHolder(private val binding:IngredientsRowLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(extendedIngredient: ExtendedIngredient){
            binding.ingredientImageView.load(BASE_IMG_URL+extendedIngredient.image){
                crossfade(600)
                error(R.drawable.ic_baseline_broken_image_24)
            }
            binding.ingredientName.text = extendedIngredient.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            binding.ingredientAmount.text = extendedIngredient.amount.toString()
            //binding.ingredientAmount.setTextColor(R.color.titleColor)
            binding.ingredientUnit.text = extendedIngredient.unit
            binding.ingredientConsistency.text = extendedIngredient.consistency
            binding.ingredientOriginal.text = extendedIngredient.original
        }
        companion object{
            fun from( parent:ViewGroup): IngredientsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IngredientsRowLayoutBinding.inflate(layoutInflater,parent,false)
                return IngredientsViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        return IngredientsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(ingredientsList[position])
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData( ingredients: List<ExtendedIngredient>){
        val recipeDiffUtil = RecipeDiffUtil(ingredientsList,ingredients)
        val diffUtilResult = DiffUtil.calculateDiff(recipeDiffUtil)
        ingredientsList=ingredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}