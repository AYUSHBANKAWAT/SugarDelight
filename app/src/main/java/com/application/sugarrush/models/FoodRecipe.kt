package com.application.sugarrush.models


import com.google.gson.annotations.SerializedName

data class FoodRecipe(
    @SerializedName("results")
    val recipes: List<Result>
)