package com.application.sugarrush.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.application.sugarrush.data.DataStoreRepository
import com.application.sugarrush.util.constants.Companion.API_KEY
import com.application.sugarrush.util.constants.Companion.DEFAULT_DIET_TYPE
import com.application.sugarrush.util.constants.Companion.DEFAULT_MEAL_TYPE
import com.application.sugarrush.util.constants.Companion.DEFAULT_RECIPE_NUMBER
import com.application.sugarrush.util.constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.application.sugarrush.util.constants.Companion.QUERY_API_KEY
import com.application.sugarrush.util.constants.Companion.QUERY_DIET
import com.application.sugarrush.util.constants.Companion.QUERY_FILL_INGREDIENTS
import com.application.sugarrush.util.constants.Companion.QUERY_NUMBER
import com.application.sugarrush.util.constants.Companion.QUERY_SEARCH
import com.application.sugarrush.util.constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject
constructor(application:Application,
            private val dataStoreRepository: DataStoreRepository):AndroidViewModel(application) {
    val mealAndDietType = dataStoreRepository.readMealAndDietType
    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE
    var networkStatus = false
    var backOnline = false
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()
    fun saveMealAndDietType(mealType:String,mealTypeId:Int,dietType:String,dietTypeId:Int){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveMealAndDietType(mealType,mealTypeId,dietType,dietTypeId)
        }
    }
    fun saveBackOnline(backOnline:Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveOnlineStatus(backOnline)
    }
     fun applyQueries():HashMap<String,String>{
        val queries:HashMap<String,String> = HashMap()
         viewModelScope.launch {
             mealAndDietType.collect{values->
                 mealType = values.selectedMealType
                 dietType = values.selectedDietType
             }
         }
        queries[QUERY_NUMBER]= DEFAULT_RECIPE_NUMBER
        queries[QUERY_API_KEY]= API_KEY
        queries[QUERY_TYPE]= mealType
        queries[QUERY_DIET]= dietType
        queries[QUERY_ADD_RECIPE_INFORMATION]="true"
        queries[QUERY_FILL_INGREDIENTS]="true"
        return queries
    }

    fun applySearchQueries(query:String):HashMap<String,String>{
        val queries : HashMap<String,String> = HashMap()
        queries[QUERY_SEARCH] = query
        queries[QUERY_NUMBER]= DEFAULT_RECIPE_NUMBER
        queries[QUERY_API_KEY]= API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION]="true"
        queries[QUERY_FILL_INGREDIENTS]="true"
        return queries
    }

    fun showNetworkStatus(){
        if(!networkStatus){
            Toast.makeText(getApplication(),"No Internet",Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        }else if( networkStatus ){
            if( backOnline ){
                Toast.makeText(getApplication(),"We're back Online",Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}