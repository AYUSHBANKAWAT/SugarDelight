package com.application.sugarrush.ui.fragments.recipe

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.sugarrush.viewModel.MainViewModel
import com.application.sugarrush.R
import com.application.sugarrush.adapter.RecipeAdapter
import com.application.sugarrush.databinding.FragmentRecipieBinding
import com.application.sugarrush.util.NetworkListener
import com.application.sugarrush.util.NetworkResult
import com.application.sugarrush.util.observeOnce
import com.application.sugarrush.viewModel.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipieFragment : Fragment(),SearchView.OnQueryTextListener {
    private val args by navArgs<RecipieFragmentArgs>()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel:RecipesViewModel
    private lateinit var networkListener: NetworkListener
    private val mAdapter by lazy {
        RecipeAdapter()
    }
    private var _binding:FragmentRecipieBinding? = null
    private val binding  get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipeViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
        //setUpMenu()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentRecipieBinding.inflate(inflater,container,false)
        //mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding.lifecycleOwner=this
        binding.mainViewModel = mainViewModel
        setUpRecyclerView()
        setHasOptionsMenu(true)
        recipeViewModel.readBackOnline.observe(viewLifecycleOwner, Observer {
            recipeViewModel.backOnline = it
        })
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect{status->
                    Log.d("Recipe fragment network listener",status.toString())
                    recipeViewModel.networkStatus = status
                    recipeViewModel.showNetworkStatus()
                    readDatabase()
                }
        }
        binding.recipesFab.setOnClickListener {
            if( recipeViewModel.networkStatus ){
                findNavController().navigate(R.id.action_recipieFragment_to_recipesBottomSheet)
            }
            else{
                recipeViewModel.showNetworkStatus()
            }

        }
        return binding.root
    }

    private  fun setUpMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object:MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.recipes_menu,menu)
                val search = menu.findItem(R.id.menu_search)
                val searchView  = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if( query!=null){
                            searchApiData(query)
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }

                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }

        })
    }



    private fun setUpRecyclerView(){
        binding.shimmerRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            showShimmerEffect()
        }
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, Observer {database->
                if( database.isNotEmpty() && args.backFromBottomSheet ){
                    Log.d("RecipeFragment","readData data is called")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                }else{
                    requestApiData()
                }
            })
        }
    }


    private fun requestApiData(){
        Log.d("RecipeFragment","API data is called")
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is NetworkResult.Success->{
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error->{
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                        ).show()
                }
                else ->{
                    showShimmerEffect()
                }
            }

        })
    }

    private fun searchApiData(searchQuery:String){
        showShimmerEffect()
        mainViewModel.searchQueries(recipeViewModel.applySearchQueries(searchQuery))
        mainViewModel.searchRecipes.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is NetworkResult.Success->{
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error->{
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else ->{
                    showShimmerEffect()
                }
            }
        })
    }

    private fun loadDataFromCache(){
        Log.d("RecipeFragment","loadDataFromCache")
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, Observer { database->
                if(database.isNotEmpty()){
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }

    private fun showShimmerEffect(){
        binding.shimmerRecyclerView.showShimmer()
    }
    private fun hideShimmerEffect(){
        binding.shimmerRecyclerView.hideShimmer()
    }

    //to avoid memory leaks


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }



}