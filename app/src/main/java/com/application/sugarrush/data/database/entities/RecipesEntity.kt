package com.application.sugarrush.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.sugarrush.models.FoodRecipe
import com.application.sugarrush.util.constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE,)
class RecipesEntity(var foodRecipe:FoodRecipe) {
    @PrimaryKey(autoGenerate = false)
    var id:Int =0
}