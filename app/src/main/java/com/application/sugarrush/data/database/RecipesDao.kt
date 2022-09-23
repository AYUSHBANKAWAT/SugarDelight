package com.application.sugarrush.data.database

import androidx.room.*
import com.application.sugarrush.data.database.entities.FavouritesEntity
import com.application.sugarrush.data.database.entities.FoodJokeEntity
import com.application.sugarrush.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipe(favouritesEntity: FavouritesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun getRecipes(): Flow<List<RecipesEntity>>

    @Query("SELECT * FROM FAVOURITE_RECIPE_TABLE ORDER BY id ASC")
    fun getFavouriteRecipes(): Flow<List<FavouritesEntity>>

    @Query("SELECT * FROM food_joke_table ORDER BY id ASC")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>

    @Delete
    suspend fun deleteFavouriteRecipe( favouritesEntity: FavouritesEntity )

    @Query("DELETE FROM favourite_recipe_table")
    suspend fun deleteAllFavouriteRecipes()





}