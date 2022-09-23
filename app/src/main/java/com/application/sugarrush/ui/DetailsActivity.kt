package com.application.sugarrush.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.navArgs
import com.application.sugarrush.R
import com.application.sugarrush.adapter.PagerAdapter
import com.application.sugarrush.data.database.entities.FavouritesEntity
import com.application.sugarrush.databinding.ActivityDetailsBinding
import com.application.sugarrush.ui.fragments.ingredients.IngredientsFragment
import com.application.sugarrush.ui.fragments.instructions.InstructionFragment
import com.application.sugarrush.ui.fragments.overview.OverviewFragment
import com.application.sugarrush.util.constants.Companion.RECIPE_RESULT_KEY
import com.application.sugarrush.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private var _binding : ActivityDetailsBinding? = null
    private val binding get() = _binding!!
    private val args  by navArgs<DetailsActivityArgs>()
    private val mainViewModel:MainViewModel by viewModels()
    private var recipeSaved = false
    private var recipeSavedId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_details)
        setSupportActionBar(binding?.toolbar)
        binding?.toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val recipeBundle = Bundle()
        recipeBundle.putParcelable(RECIPE_RESULT_KEY,args.result)
        PagerAdapter(this,recipeBundle,binding.detailsTabLayout,binding.detailsViewPager).apply {
            addFragment(OverviewFragment(),"Overview")
            addFragment(IngredientsFragment(),"Ingredients")
            addFragment(InstructionFragment(),"Instruction")
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if( item.itemId == android.R.id.home ){
            finish()
        }
        else if( item.itemId == R.id.save_to_favorites_menu && !recipeSaved){
            saveToFavourites(item)
        }else if( item.itemId == R.id.save_to_favorites_menu && recipeSaved ){
            removeFromFavourite(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavourites(item: MenuItem) {
        val favouritesEntity =
            FavouritesEntity(
                0,
                args.result
            )
        mainViewModel.insertFavouriteRecipe((favouritesEntity))
        changeMenuItemColor(item,R.color.yellow)
        showSnackBar("Item Saved")
    }

    private fun showSnackBar(s: String) {
        Snackbar.make(
            binding.detailsLayout,
            s,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
        recipeSaved=true

    }

    private fun removeFromFavourite(item:MenuItem){
        val favouritesEntity = FavouritesEntity(
            0,
            args.result
        )
        mainViewModel.deleteFavouriteRecipe(favouritesEntity)
        showSnackBar("Recipe Deleted")
        changeMenuItemColor(item,R.color.white)
        recipeSaved=false
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this,color))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu,menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedItem(menuItem)
        return true
    }

    private fun checkSavedItem(item: MenuItem?) {
        mainViewModel.readFavoriteRecipes.observe(this,Observer{favouriteEntity->
            try {
                for( savedRecipe in favouriteEntity ){
                    if( savedRecipe.recipe.id == args.result.id ){
                        changeMenuItemColor(item!!,R.color.yellow)
                        recipeSavedId = savedRecipe.id
                        recipeSaved=true
                        break
                    }
                    else {
                        changeMenuItemColor(item!!,R.color.white)
                    }
                }
            }catch (e:Exception){
                Log.d("Details Activity",e.message.toString())
            }

        })
    }
}