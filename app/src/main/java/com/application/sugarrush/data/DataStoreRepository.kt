package com.application.sugarrush.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.application.sugarrush.util.constants.Companion.PREFERENCES_BACK_ONLINE_KEY
import com.application.sugarrush.util.constants.Companion.DEFAULT_DIET_TYPE
import com.application.sugarrush.util.constants.Companion.DEFAULT_MEAL_TYPE
import com.application.sugarrush.util.constants.Companion.PREFERENCES_DIET_TYPE
import com.application.sugarrush.util.constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.application.sugarrush.util.constants.Companion.PREFERENCES_MEAL_TYPE
import com.application.sugarrush.util.constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.application.sugarrush.util.constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository@Inject constructor(@ApplicationContext private val context : Context) {
    private object PreferenceKeys{
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
        val backOnlineKey =  booleanPreferencesKey(PREFERENCES_BACK_ONLINE_KEY)
    }
    private val Context.dataStore by preferencesDataStore(name =  PREFERENCES_NAME)
    private val dataStore = context.dataStore

    suspend fun saveOnlineStatus(backOnline:Boolean){
        dataStore.edit { preferences->
            preferences[PreferenceKeys.backOnlineKey] = backOnline
        }
    }

    suspend fun saveMealAndDietType(mealType:String,mealTypeId:Int,dietType:String,dietTypeId:Int){
        dataStore.edit {preferences->
            preferences[PreferenceKeys.selectedMealType]=mealType
            preferences[PreferenceKeys.selectedMealTypeId]=mealTypeId
            preferences[PreferenceKeys.selectedDietType]=dietType
            preferences[PreferenceKeys.selectedDietTypeId]=dietTypeId
        }
    }
    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch {exception->
            if( exception is IOException ){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map {preferences->
            val selectedMealType = preferences[PreferenceKeys.selectedMealType]?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId]?:0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType]?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId]?:0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }
    val readBackOnline:Flow<Boolean> = dataStore.data
        .catch {exception->
        if( exception is IOException ){
            emit(emptyPreferences())
        }
        else{
            throw  exception
         }
       }
        .map { preferences->
            val backToOnline = preferences[PreferenceKeys.backOnlineKey]?:false
            backToOnline
        }
}
data class MealAndDietType(
    val selectedMealType:String,
    val selectedMealTypeId: Int,
    val selectedDietType:String,
    val selectedDietTypeId: Int
)