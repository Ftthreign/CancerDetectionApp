package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import com.dicoding.asclepius.R
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


class ImageClassifierHelper(
    private var threshold : Float = 0.2f,
    private var maxResult: Int = 3,
    private val modelName : String = "cancer_classification.tflite",
    val context: Context,
    val imageClassifierListener: ImageClassifierListener
) {

    private var imageClassifier : ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(maxResult)
            .setScoreThreshold(threshold)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
            .build()
        optionsBuilder.setBaseOptions(baseOptionsBuilder)

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e : IllegalStateException) {
            imageClassifierListener.onError(context.getString(R.string.classifier_failed))
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        // TODO: mengklasifikasikan imageUri dari gambar statis.
    }

    interface ImageClassifierListener {
        fun onError(error :String)
        fun onResults(
            results : List<Classifications>,
            inferenceTime : Long
        )
    }

}