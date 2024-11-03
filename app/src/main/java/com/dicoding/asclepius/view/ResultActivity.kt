package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.CancerHistoryEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.fragment.HomeFragment
import com.dicoding.asclepius.view.viewmodel.ResultViewModel
import com.dicoding.asclepius.view.viewmodel.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)

        setContentView(binding.root)

        analyzeImage()
    }


    private fun analyzeImage() {
        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        val imageUri = Uri.parse(intent.getStringExtra(IMAGE_URI))
        imageUri.let { uriImage ->
            binding.resultImage.setImageURI(uriImage)
        }
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@ResultActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Classifications>, inferenceTime: Long) {
                    runOnUiThread {
                        results.let { image ->
                            if(image.isNotEmpty() && image[0].categories.isNotEmpty()) {
                                println(image)
                                val sortedCategories = image[0].categories.sortedByDescending { it.score }
                                val isCancer = sortedCategories[0].label
                                val confidenceScore = NumberFormat
                                    .getPercentInstance()
                                    .format(sortedCategories[0].score)
                                    .trim()
                                binding.resultText.text = getString(R.string.result_text, isCancer, confidenceScore)
                                binding.saveButton.setOnClickListener {
                                    imageUri.let {
                                        val cancerHistory = CancerHistoryEntity(
                                            result = getString(R.string.item_result_history, isCancer),
                                            confidenceScore = getString(R.string.item_score_history, confidenceScore),
                                            pathImage = setUriAsFile(it).toString()
                                        )
                                        viewModel.insertNewCancerHistory(cancerHistory)
                                        Toast.makeText(this@ResultActivity, "Result is saved to History", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                }
                                binding.closeButton.setOnClickListener {
                                    supportFragmentManager
                                        .beginTransaction()
                                        .replace(R.id.fragment_home_container, HomeFragment())
                                        .addToBackStack(null)
                                        .commit()
                                }
                            } else {
                                Toast.makeText(this@ResultActivity, "No result Attached", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            }
        )
        imageClassifierHelper.classifyStaticImage(imageUri)
    }

    private fun setUriAsFile(imageUri: Uri): String? {
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val destinationFile = File(this.filesDir, fileName)
        return try {
            val inputStream: InputStream? = this.contentResolver.openInputStream(imageUri)
            val outputStream: OutputStream = FileOutputStream(destinationFile)

            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream?.read(buffer).also { length = it ?: -1 } != -1) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream?.close()

            destinationFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        const val IMAGE_URI = "image_uri"
    }

}