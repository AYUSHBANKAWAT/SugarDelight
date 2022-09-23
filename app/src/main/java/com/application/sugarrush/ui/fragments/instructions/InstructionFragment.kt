package com.application.sugarrush.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.application.sugarrush.R
import com.application.sugarrush.databinding.FragmentIngredientsBinding
import com.application.sugarrush.databinding.FragmentInstructionBinding
import com.application.sugarrush.models.Result
import com.application.sugarrush.util.constants.Companion.RECIPE_RESULT_KEY


class InstructionFragment : Fragment() {
    private var _binding:FragmentInstructionBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val args = arguments
        val myBundle:Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        _binding = FragmentInstructionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner  = this
        val websiteUrl = myBundle?.sourceUrl
        binding.instructionWebView.apply {
            webViewClient = object :WebViewClient(){}
            loadUrl(websiteUrl!!)

        }
        return binding.root
    }
}