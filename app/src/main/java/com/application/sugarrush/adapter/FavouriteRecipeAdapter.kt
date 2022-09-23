package com.application.sugarrush.adapter

import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.sugarrush.R
import com.application.sugarrush.data.database.entities.FavouritesEntity
import com.application.sugarrush.databinding.FavouriteRecipeRowLayoutBinding
import com.application.sugarrush.ui.fragments.favourite.favouriteReciepeFragment
import com.application.sugarrush.ui.fragments.favourite.favouriteReciepeFragmentDirections
import com.application.sugarrush.util.RecipeDiffUtil
import com.application.sugarrush.viewModel.MainViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class FavouriteRecipeAdapter(private val requireFragment:FragmentActivity,private val mainViewModel: MainViewModel):RecyclerView.Adapter<FavouriteRecipeAdapter.FavouriteViewHolder>(),ActionMode.Callback {
    private var favouritesList = emptyList<FavouritesEntity>()
    private var multiSelection = false
    private lateinit var view:View
    private var myViewHolder = arrayListOf<FavouriteViewHolder>()
    private var selectedRecipe = arrayListOf<FavouritesEntity>()
    private lateinit var mActionMode:ActionMode
    class FavouriteViewHolder(private val binding : FavouriteRecipeRowLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(favouritesEntity: FavouritesEntity){
            binding.favoritesEntity=favouritesEntity
            binding.executePendingBindings()
        }

        companion object{
            fun from( parent:ViewGroup): FavouriteRecipeAdapter.FavouriteViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavouriteRecipeRowLayoutBinding.inflate(layoutInflater,parent,false)
                return FavouriteViewHolder(binding)
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        myViewHolder.add(holder)
        view=holder.itemView.rootView
        val selectedRecipe = favouritesList[position]
        holder.bind(selectedRecipe)
        val view = holder.itemView.findViewById<ConstraintLayout>(R.id.favouriteRecipesRowLayout)

        view.setOnClickListener {
            if(multiSelection){
                applySelection(holder,selectedRecipe)
            }else{
                val action = favouriteReciepeFragmentDirections.actionFavouriteReciepeFragmentToDetailsActivity(favouritesList[position].recipe)
                holder.itemView.findNavController().navigate(action)
            }
        }
        view.setOnLongClickListener {
            if( !multiSelection ){
                multiSelection=true
                requireFragment.startActionMode(this)
                applySelection(holder, selectedRecipe)
                true
            }else{
                multiSelection=false
                false
            }

        }


    }

    override fun getItemCount(): Int {
        return favouritesList.size
    }

    private fun applyActionModeTitle(){
        when(selectedRecipe.size){
            0->{
                mActionMode.finish()
            }
            1->{
                mActionMode.title = "${selectedRecipe.size} item selected"
            }
            else ->{
                mActionMode.title = "${selectedRecipe.size} items selected"
            }
        }
    }

    private fun applySelection(holder: FavouriteViewHolder,currentRecipe:FavouritesEntity){
        if( selectedRecipe.contains(currentRecipe) ){
            selectedRecipe.remove(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
            applyActionModeTitle()
        }else{
            selectedRecipe.add(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundLightColor,R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: FavouriteViewHolder,backgroundColor:Int,strokeColor:Int){
        val view = holder.itemView.findViewById<ConstraintLayout>(R.id.favouriteRecipesRowLayout)
        view.setBackgroundColor(ContextCompat.getColor(requireFragment,backgroundColor))
        val cardView = holder.itemView.findViewById<MaterialCardView>(R.id.favorite_row_cardView)
        cardView.strokeColor = ContextCompat.getColor(requireFragment,strokeColor)
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favourite_contextual_menu,menu)
        mActionMode=actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
        if( p1?.itemId == R.id.delete_favourite_recipe_menu){
            selectedRecipe.forEach{
                mainViewModel.deleteFavouriteRecipe(it)
            }
            showSnackBar("${selectedRecipe.size} item deleted")
            multiSelection=false
            selectedRecipe.clear()
            p0?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(p0: ActionMode?) {
        myViewHolder.forEach {holder->
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
        }
        multiSelection=false
        selectedRecipe.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color:Int){
        requireFragment.window.statusBarColor = ContextCompat.getColor(requireFragment,color)
    }

    fun setData( favourites: List<FavouritesEntity>){
        val recipeDiffUtil = RecipeDiffUtil(favouritesList,favourites)
        val diffUtilResult = DiffUtil.calculateDiff(recipeDiffUtil)
        favouritesList=favourites
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(msg:String){
        Snackbar.make(
            view,
            msg,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }

}