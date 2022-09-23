package com.application.sugarrush.ui.fragments.recipe.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.application.sugarrush.R
import com.application.sugarrush.databinding.FragmentRecipieBinding
import com.application.sugarrush.databinding.RecipesBottomSheetBinding
import com.application.sugarrush.util.constants.Companion.DEFAULT_DIET_TYPE
import com.application.sugarrush.util.constants.Companion.DEFAULT_MEAL_TYPE
import com.application.sugarrush.viewModel.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception

class RecipesBottomSheet : BottomSheetDialogFragment() {
    private lateinit var recipesViewModel: RecipesViewModel
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0
    private var _binding: RecipesBottomSheetBinding? = null
    private val binding  get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= RecipesBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        recipesViewModel.mealAndDietType.asLiveData().observe(viewLifecycleOwner, Observer {values->
            mealTypeChip = values.selectedMealType
            dietTypeChip = values.selectedDietType
            updateChip(values.selectedMealTypeId,binding.mealTypeChipGroup)
            updateChip(values.selectedDietTypeId,binding.dietTypeChipGroup)
        })
        binding.mealTypeChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip = group.findViewById<Chip>(checkedIds[0])
            val selectedMealType = chip.text.toString().lowercase()
            mealTypeChip = selectedMealType
            mealTypeChipId = checkedIds[0]
        }
        binding.dietTypeChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip = group.findViewById<Chip>(checkedIds[0])
            val selectedDietType = chip.text.toString().lowercase()
            dietTypeChip = selectedDietType
            dietTypeChipId = checkedIds[0]
        }
        binding.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(mealTypeChip,mealTypeChipId,dietTypeChip,dietTypeChipId)
            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipieFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if( chipId!=0 ){
            try{
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            }catch (e:Exception){
                Log.d("Recipe Bottom Sheet",e.message.toString())
            }
        }
    }

}