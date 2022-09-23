package com.application.sugarrush.data

import com.application.sugarrush.data.network.FoodRecipeApi
import com.application.sugarrush.models.FoodJoke
import com.application.sugarrush.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi
) {
    suspend fun getRecipes(queries:Map<String,String>):Response<FoodRecipe>{
        return foodRecipeApi.getRecipes(queries)
    }
    suspend fun searchRecipes( queries: Map<String, String> ):Response<FoodRecipe>{
        return foodRecipeApi.searchRecipe(queries)
    }

    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
        return foodRecipeApi.getFoodJoke(apiKey)
    }
}