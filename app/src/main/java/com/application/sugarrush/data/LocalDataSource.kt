package com.application.sugarrush.data

import com.application.sugarrush.data.database.RecipesDao
import com.application.sugarrush.data.database.entities.FavouritesEntity
import com.application.sugarrush.data.database.entities.FoodJokeEntity
import com.application.sugarrush.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipeDao:RecipesDao
){
    fun readDatabase(): Flow<List<RecipesEntity>>{
       return recipeDao.getRecipes()
    }

    fun readFavouriteRecipes(): Flow<List<FavouritesEntity>>{
        return recipeDao.getFavouriteRecipes()
    }

    fun readFoodJoke(): Flow<List<FoodJokeEntity>> {
        return recipeDao.readFoodJoke()
    }

    suspend fun insertRecipes( recipesEntity: RecipesEntity){
        recipeDao.insertRecipe(recipesEntity)
    }

    suspend fun insertFavouriteRecipes( recipesEntity: FavouritesEntity){
        recipeDao.insertFavouriteRecipe(recipesEntity)
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) {
        recipeDao.insertFoodJoke(foodJokeEntity)
    }

    suspend fun deleteRecipe( favouritesEntity: FavouritesEntity ){
        recipeDao.deleteFavouriteRecipe(favouritesEntity)
    }
    suspend fun deleteAllFavouriteRecipe(){
        recipeDao.deleteAllFavouriteRecipes()
    }
}