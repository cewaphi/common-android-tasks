package com.example.mlkitlanguagedetection.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mlkitlanguagedetection.R
import com.example.mlkitlanguagedetection.databinding.FragmentHomeBinding
import com.google.mlkit.nl.languageid.LanguageIdentification
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val languageIdentifier get() = LanguageIdentification.getClient()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDetect.setOnClickListener {
            try {
                languageIdentifier.identifyLanguage(binding.inputText.text.toString())
                    .addOnSuccessListener {
                        if (it.contains("und", ignoreCase = true)) {
                            Toast.makeText(requireContext(), "What language is this?!", Toast.LENGTH_SHORT).show()
                            binding.detectedLanguage.text = "UNKNOWN"
                        } else {
                            Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
                            binding.detectedLanguage.text = Locale(it).displayName
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failure! :(", Toast.LENGTH_SHORT).show()
                    }
                languageIdentifier.identifyPossibleLanguages(binding.inputText.text.toString())
                    .addOnSuccessListener { identifiedLanguages ->
                        binding.detectedLanguages.text = identifiedLanguages.map {
                            "${it.languageTag} (${"%.3f".format(it.confidence)})"
                        }.joinToString("\n")
                    }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: $e", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}