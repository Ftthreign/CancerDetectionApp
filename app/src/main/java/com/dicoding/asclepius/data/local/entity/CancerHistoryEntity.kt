package com.dicoding.asclepius.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity("cancerHistory")
data class CancerHistoryEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id : Int = 0,

    @ColumnInfo("result")
    val result : String,
    @ColumnInfo("confidenceScore")
    val confidenceScore : String,
    @ColumnInfo("pathImage")
    val pathImage : String

) : Parcelable