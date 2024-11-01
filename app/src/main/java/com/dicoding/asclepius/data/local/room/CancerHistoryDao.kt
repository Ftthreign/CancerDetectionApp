package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.CancerHistoryEntity

@Dao
interface CancerHistoryDao {
    @Query("SELECT * FROM cancerHistory ORDER BY id DESC")
    fun getCancerHistory() : LiveData<List<CancerHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCancerHistory(history : CancerHistoryEntity)

    @Delete
    suspend fun deleteCancerHistory(history: CancerHistoryEntity)
}