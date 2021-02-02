package com.example.datasharing.ui.import

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.datasharing.databinding.FragmentImportBinding
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.android.synthetic.main.fragment_import.*
import java.io.File
import java.io.BufferedReader

class ImportFragment : Fragment() {

    private lateinit var galleryViewModel: ImportViewModel
    private var _binding: FragmentImportBinding? = null
    private val args: ImportFragmentArgs by navArgs()
    private var parsedUri: Uri? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.uri?.let {
            parsedUri = Uri.parse(it)
            if (it.isNotBlank() && !(it == "null")) {
                Toast.makeText(requireContext(), "Parsed Uri: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        galleryViewModel =
                ViewModelProvider(this).get(ImportViewModel::class.java)

        _binding = FragmentImportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.importBtn
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        parsedUri?.let { parseCsvFromUri(it) }
        binding.importBtn.setOnClickListener {

        }
        
        return root
    }

    private fun parseCsvFromUri(uri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        inputStream?.let {
            try {
                val data = csvReader().readAllWithHeader(it)
                binding.textUri.text = data.joinToString()
            } catch (e: Exception) {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}