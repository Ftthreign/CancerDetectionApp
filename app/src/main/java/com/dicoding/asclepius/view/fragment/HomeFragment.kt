package com.dicoding.asclepius.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.view.ResultActivity
import com.dicoding.asclepius.view.viewmodel.MainViewModel
import com.dicoding.asclepius.view.viewmodel.ViewModelFactory
import com.yalantis.ucrop.UCrop
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>() {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.curImgUri.observe(viewLifecycleOwner) {
            showImage(it)
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.analyzeButton.setOnClickListener {
            viewModel.curImgUri.value?.let {
                moveToResult(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri : Uri? ->
        if (uri != null) {
            launchUcrop(uri)
        } else {
            Log.d(TAG, "No media selected, please pick one picture")
        }
    }

    private val launcherUCrop = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { data ->
        when(data.resultCode) {
            RESULT_OK -> {
                viewModel.setCurrentImage(data.data?.let {
                    UCrop.getOutput(it)
                })
            }
            UCrop.RESULT_ERROR -> {
                UCrop.getError(data.data!!)?.let {
                    showToast(it.message.toString())
                }
            }
        }
    }

    private fun launchUcrop(uri: Uri) {
        val uriPath = Uri.fromFile(File.createTempFile("cropped_", ".jpg", requireActivity().cacheDir))
        launcherUCrop.launch(
            UCrop.of(uri, uriPath)
                .getIntent(requireActivity())
        )
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage(uri: Uri?) {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        uri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }


    private fun moveToResult(uri : Uri) {
        val intent = Intent(requireActivity(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.IMAGE_URI, uri.toString())
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }

}