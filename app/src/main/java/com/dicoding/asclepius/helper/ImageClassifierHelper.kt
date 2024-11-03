package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import com.dicoding.asclepius.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


@Suppress("DEPRECATION")
class ImageClassifierHelper(
    private var threshold : Float = 0.2f,
    private var maxResult: Int = 3,
    private val modelName : String = "cancer_classification.tflite",
    val context: Context,
    val classifierListener: ClassifierListener
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
            classifierListener.onError(context.getString(R.string.classifier_failed))
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        // TODO: mengklasifikasikan imageUri dari gambar statis.
        if(imageClassifier == null) setupImageClassifier()

        val imageProcessor = ImageProcessor.Builder()
            .add(CastOp(DataType.FLOAT32))
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
            var inferenceTime = SystemClock.uptimeMillis()
            val res = imageClassifier?.classify(tensorImage)
            inferenceTime = SystemClock.uptimeMillis() - inferenceTime
            if (res != null) {
                classifierListener.onResults(
                    res,
                    inferenceTime
                )
            }
        }
    }

    interface ClassifierListener {
        fun onError(error :String)
        fun onResults(
            results : List<Classifications>,
            inferenceTime : Long
        )
    }
}