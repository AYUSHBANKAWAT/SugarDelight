package com.application.sugarrush.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.sugarrush.models.Result
import com.application.sugarrush.util.constants.Companion.FAVOURITE_RECIPE_TABLE

@Entity(tableName = FAVOURITE_RECIPE_TABLE)
data class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var recipe:Result
)