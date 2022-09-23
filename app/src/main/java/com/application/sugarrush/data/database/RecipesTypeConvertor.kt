package com.application.sugarrush.data.database

import androidx.room.TypeConverter
import com.application.sugarrush.models.FoodRecipe
import com.application.sugarrush.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConvertor {

    var gson = Gson()
    @TypeConverter
    fun foodRecipeToString( foodRecipe: FoodRecipe ):String{
        return gson.toJson(foodRecipe)
    }
    @TypeConverter
    fun stringToFoodRecipe( data:String ):FoodRecipe{
        val list = object :TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data,list)
    }
    @TypeConverter
    fun resultToString(data:Result):String{
        return gson.toJson(data)
    }
    @TypeConverter
    fun stringToResult(data:String):Result{
        val list = object :TypeToken<Result>(){}.type
        return gson.fromJson(data,list)
    }
}