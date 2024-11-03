package com.dicoding.asclepius.view.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.AppRepository

class MainViewModel(private val appRepository: AppRepository) : ViewModel() {
    private var _curImgUri = MutableLiveData<Uri?>()
    val curImgUri : MutableLiveData<Uri?> = _curImgUri

    fun setCurrentImage(uri: Uri?) {
        _curImgUri.value = uri
    }

    fun getNewsData() = appRepository.getNews()

}