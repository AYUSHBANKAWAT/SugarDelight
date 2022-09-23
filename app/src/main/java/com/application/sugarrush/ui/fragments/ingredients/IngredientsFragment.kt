package com.application.sugarrush.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.sugarrush.adapter.IngredientsAdapter
import com.application.sugarrush.databinding.FragmentIngredientsBinding
import com.application.sugarrush.models.Result
import com.application.sugarrush.util.constants.Companion.RECIPE_RESULT_KEY


class IngredientsFragment : Fragment() {
    private var _binding : FragmentIngredientsBinding? =null
    private val binding get() = _binding!!
    private val mAdapter :IngredientsAdapter by lazy {
        IngredientsAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        val args = arguments
        val myBundle : Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        setUpRecyclerView(binding.ingredientsRecyclerview)
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }
        return binding.root
    }
    private fun setUpRecyclerView( recyclerView: RecyclerView ){
        recyclerView.apply {
            adapter=mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


}