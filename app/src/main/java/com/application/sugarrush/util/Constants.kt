package com.application.sugarrush.util

class constants {
    companion object{
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMG_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        const val API_KEY = "cfabd073c5674a49a0b001457efe20b1"
        const val RECIPE_RESULT_KEY = "recipeBundle"
        //API_QUERY
        const val QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        //ROOM DATABASE
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
        const val FAVOURITE_RECIPE_TABLE="favourite_recipe_table"
        const val FOOD_JOKE_TABLE = "food_joke_table"
        //BOTTOM SHEET PREFERENCE
        const val DEFAULT_RECIPE_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"
        const val  PREFERENCES_MEAL_TYPE = "mealType"
        const val  PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val  PREFERENCES_DIET_TYPE = "dietType"
        const val  PREFERENCES_DIET_TYPE_ID = "dietTypeId"


        const val PREFERENCES_NAME = "foody preferences"
        const val PREFERENCES_BACK_ONLINE_KEY = "backOnlineKey"

    }
}