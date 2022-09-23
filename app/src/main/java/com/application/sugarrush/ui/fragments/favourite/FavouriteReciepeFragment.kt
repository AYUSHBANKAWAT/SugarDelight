package com.application.sugarrush.ui.fragments.favourite

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.sugarrush.R
import com.application.sugarrush.adapter.FavouriteRecipeAdapter
import com.application.sugarrush.databinding.FragmentFavouriteReciepeBinding
import com.application.sugarrush.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class favouriteReciepeFragment : Fragment() {
    private var _binding:FragmentFavouriteReciepeBinding?=null
    private val binding  get() = _binding!!
    private val mainViewModel:MainViewModel by viewModels()
    private val mAdapter:FavouriteRecipeAdapter by  lazy { FavouriteRecipeAdapter(requireActivity(),mainViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentFavouriteReciepeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        setUpRecyclerView(binding.favoriteRecipesRecyclerView)
        binding.mainViewModel=mainViewModel
        binding.mAdapter=mAdapter
        setHasOptionsMenu(true)
        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner, Observer {favouriteEntity->
            mAdapter.setData(favouriteEntity)
            Log.d("FavouriteRecipeFragment",favouriteEntity.size.toString())
        })
        return binding.root
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView){
        recyclerView.apply {
            adapter=mAdapter
            layoutManager=LinearLayoutManager(requireContext())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourite_recipe_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if( item.itemId==R.id.delete_all_favourite_recipe_menu ){
            mainViewModel.deleteAllFavouriteRecipes()
            showSnackBar("All recipes removed")
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
        mAdapter.clearContextualActionMode()
    }

    private fun showSnackBar(msg:String){
        Snackbar.make(
            binding.root,
            msg,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

}