package com.application.sugarrush.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.application.sugarrush.data.Repository
import com.application.sugarrush.data.database.entities.FavouritesEntity
import com.application.sugarrush.data.database.entities.FoodJokeEntity
import com.application.sugarrush.data.database.entities.RecipesEntity
import com.application.sugarrush.models.FoodJoke
import com.application.sugarrush.models.FoodRecipe
import com.application.sugarrush.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository: Repository,
    application: Application,)  :AndroidViewModel(application) {
    /* *
     * Room
     * */
    val readRecipes:LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavouritesEntity>> = repository.local.readFavouriteRecipes().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()


    private fun insertRecipes(recipesEntity: RecipesEntity)=viewModelScope.launch(Dispatchers.IO){
        repository.local.insertRecipes(recipesEntity)
    }

    fun insertFavouriteRecipe(favoritesEntity: FavouritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavouriteRecipes(favoritesEntity)
        }

    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }

    fun deleteFavouriteRecipe(favoritesEntity: FavouritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteRecipe(favoritesEntity)
        }

    fun deleteAllFavouriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavouriteRecipe()
        }

    /* *
     * Retrofit
     * */
    var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipes :MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()
    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getReciepeSafeCall(queries)
    }

    fun searchQueries( searchQueries:Map<String,String> ) = viewModelScope.launch {
        searchRecipeSafeCall(searchQueries)
    }

    fun getFoodJoke(api:String) = viewModelScope.launch {
        getFoodJokeSafeCall(api)
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternet()) {
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke = foodJokeResponse.value!!.data
                if(foodJoke != null){
                    offlineCacheFoodJoke(foodJoke)
                }
            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error("Recipes not found.",null)
            }
        } else {
            foodJokeResponse.value = NetworkResult.Error("No Internet Connection.",null)
        }
    }

    private suspend fun searchRecipeSafeCall(searchQueries: Map<String, String>) {
        searchRecipes.value = NetworkResult.Loading()
        if( hasInternet() ){
            try{
                var response = repository.remote.searchRecipes(searchQueries)
                searchRecipes.value = handleRecipeResponse(response)
            }catch (e:Exception){
                searchRecipes.value = NetworkResult.Error("Recipe Not Found",null)
                Log.i("MainViewModel",e.message.toString())
            }
        }else{
            searchRecipes.value = NetworkResult.Error("No Internet",null)
        }
    }

    private fun hasInternet():Boolean{
            val connectivityManager = getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork?:return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                else ->false
            }
        }
    private suspend fun getReciepeSafeCall(queries:Map<String,String>){
        recipesResponse.value = NetworkResult.Loading()
        if( hasInternet() ){
            try{
                var response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleRecipeResponse(response)
                val foodRecipe = recipesResponse.value!!.data
                if( foodRecipe!=null ){
                    offlineCaching(foodRecipe)
                }

            }catch (e:Exception){
                recipesResponse.value = NetworkResult.Error("Recipe Not Found",null)
                Log.i("MainViewModel",e.message.toString())
            }
        }else{
            recipesResponse.value = NetworkResult.Error("No Internet",null)
        }
    }

    private fun offlineCaching(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    private fun handleRecipeResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("time out error",null)
            }
            response.code()==402->{
                return NetworkResult.Error("API Key is Limited",null)
            }
            response.body()!!.recipes.isEmpty()->{
                return NetworkResult.Error("Recipe Not Found",null)
            }
            response.isSuccessful->{
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else ->return NetworkResult.Error(response.message().toString(),null)
        }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout",null)
            }
            response.code() == 402 -> {
                NetworkResult.Error("API Key Limited.",null)
            }
            response.isSuccessful -> {
                val foodJoke = response.body()
                NetworkResult.Success(foodJoke!!)
            }
            else -> {
                NetworkResult.Error(response.message(),null)
            }
        }
    }


}