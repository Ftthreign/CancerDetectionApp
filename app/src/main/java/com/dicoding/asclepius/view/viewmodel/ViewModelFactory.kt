package com.dicoding.asclepius.view.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.AppRepository
import com.dicoding.asclepius.di.Injection

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val appRepository: AppRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(appRepository) as T
        } else if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(appRepository) as T
        }
        throw IllegalArgumentException("${modelClass.name} is not part of the viewModel, please create view model")
    }

    companion object {
        @Volatile
        private var instance : ViewModelFactory? = null
        fun getInstance(context : Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                val appRepository = Injection.provideRepository(context)
                instance ?: ViewModelFactory(appRepository)
            }.also { instance = it }
        }
}