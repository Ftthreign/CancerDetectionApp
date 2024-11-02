package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.CancerHistoryEntity
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [CancerHistoryEntity::class], version = 1, exportSchema = false)
abstract class CancerHistoryDatabase : RoomDatabase(){
    abstract fun cancerHistoryDao() : CancerHistoryDao

    companion object {
        @Volatile
        private var instance : CancerHistoryDatabase? = null
        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context) : CancerHistoryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    CancerHistoryDatabase::class.java,
                    "CancerHistory.db"
                ).build()
            }
    }
    }
