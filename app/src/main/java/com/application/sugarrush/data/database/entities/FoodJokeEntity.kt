package com.application.sugarrush.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.sugarrush.models.FoodJoke
import com.application.sugarrush.util.constants.Companion.FOOD_JOKE_TABLE


@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    @Embedded
    var foodJoke:FoodJoke
) {
    @PrimaryKey(autoGenerate = true)
    var id:Int=0

}