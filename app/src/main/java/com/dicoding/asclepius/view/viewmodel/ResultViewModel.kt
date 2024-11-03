package com.dicoding.asclepius.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.AppRepository
import com.dicoding.asclepius.data.local.entity.CancerHistoryEntity
import kotlinx.coroutines.launch

class ResultViewModel(private val appRepository: AppRepository) : ViewModel(){
    fun getCancerHistory() = appRepository.getCancerHistory()

    fun deleteCancerHistory(cancerHistoryEntity: CancerHistoryEntity) {
        viewModelScope.launch {
            appRepository.deleteCancerHistory(cancerHistoryEntity)
        }
    }

    fun insertNewCancerHistory(cancerHistoryEntity: CancerHistoryEntity) {
        viewModelScope.launch {
            appRepository.insertCancerHistory(cancerHistoryEntity)
        }
    }
}