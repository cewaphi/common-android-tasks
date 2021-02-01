package com.example.profilepicture.ui.home

import android.app.Activity
import android.content.Intent
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
import com.example.profilepicture.MainActivity
import com.example.profilepicture.databinding.FragmentHomeBinding
import com.example.profilepicture.utils.InternalMemory
import com.example.profilepicture.utils.openImage
import com.example.profilepicture.utils.setImageFromUri

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    var imageUri: Uri? = null
    val mainActivity: MainActivity
    get() = requireActivity() as MainActivity

    companion object {
        const val SELECT_PICTURE = 1
        const val SAVED_FILE_NAME = "private_image"
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        setupListeners()

        return root
    }

    private fun setupListeners() {
        setupSelectImagelistener()
        setupAssignImageToHeaderListener()
        setupSaveToMemoryStreamListener()
        setupOpenFileFromInternalFileListener()

        setupResetListener()
    }

    private fun setupSelectImagelistener() {
        binding.selectPicture.setOnClickListener {
            startActivityForResult(
                Intent().openImage(),
                SELECT_PICTURE
            )
        }
    }

    private fun setupAssignImageToHeaderListener() {
        binding.assignCachedToHeader.setOnClickListener {
            imageUri?.let {
                mainActivity.setImageToHeader(it)
            } ?: Toast.makeText(requireContext(), "No image selected yet ?!", Toast.LENGTH_LONG).show()
        }
    }

    fun setupSaveToMemoryStreamListener() {
        binding.savePrivateStream.setOnClickListener {
            imageUri?.let {
                InternalMemory.savePickedImageFromStream(it, SAVED_FILE_NAME, requireContext())
            }

        }
    }

    fun setupOpenFileFromInternalFileListener() {
        binding.assignFromMemoryFileToHeader.setOnClickListener {
            InternalMemory.setImageFromInternalFile(requireContext(), SAVED_FILE_NAME,
            mainActivity.headerImage)
        }
    }

    fun setupResetListener() {
        binding.resetHeader.setOnClickListener {
            mainActivity.resetHeaderImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            val uri = data?.data
            uri?.let {
                imageUri = it
                setImageFromUri(it, binding.imageHome)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}