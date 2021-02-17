package com.example.datasharing.ui.export

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.datasharing.databinding.FragmentExportBinding
import com.example.datasharing.utils.Exporter
import com.example.datasharing.utils.getUriForFile
import com.example.datasharing.utils.shareCsv
import java.util.*

class ExportFragment : Fragment() {

    private lateinit var slideshowViewModel: ExportViewModel
    private var _binding: FragmentExportBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(ExportViewModel::class.java)

        _binding = FragmentExportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.exportBtn
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        binding.exportBtn.setOnClickListener {
            val file = Exporter.getTemporaryFile(requireContext(), "example_${Date().time}")
            Exporter.writeSampleCsv(file)
            val uri = file.getUriForFile(requireContext())
            startActivity(
                Intent.createChooser(
                    Intent().shareCsv(uri),
                    "Sharing example"
                )
            )
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}