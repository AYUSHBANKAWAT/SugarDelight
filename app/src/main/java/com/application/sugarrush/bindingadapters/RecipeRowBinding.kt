package com.application.sugarrush.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.application.sugarrush.R
import com.application.sugarrush.models.Result
import com.application.sugarrush.ui.fragments.recipe.RecipieFragmentDirections
import org.jsoup.Jsoup
import java.lang.Exception

class RecipeRowBinding {
    companion object{

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeCLickListener(recipeRowLayout:ConstraintLayout,result:Result){
            recipeRowLayout.setOnClickListener {
                try {
                    val action = RecipieFragmentDirections.actionRecipieFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                } catch(e:Exception){
                    Log.d("Recipe Row Binding",e.message.toString())
                }
            }
        }

        @BindingAdapter("loadImage")
        @JvmStatic
        fun loadImage(imageView: ImageView,img:String){
            imageView.load(img){
                crossfade(600)
                error(R.drawable.ic_baseline_broken_image_24)
            }
        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(textView:TextView,likes:Int){
            textView.text = likes.toString()
        }
        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic
        fun  setNumberOfMinutes(textView: TextView,min:Int){
            textView.text = min.toString()
        }

        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view:View,vegan:Boolean,){
            if(vegan){
                when(view){
                    is TextView->{
                        view.setTextColor(
                            ContextCompat.getColor(view.context, R.color.green)
                        )
                    }
                    is ImageView ->{
                        view.setColorFilter(
                            ContextCompat.getColor(view.context,R.color.green)
                        )
                    }
                }
            }
        }
        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml( textView: TextView,description:String? ){
            if( !description.isNullOrEmpty() ){
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }
    }
}