package com.example.reminder

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "reminderTable")
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long? = null,
    @ColumnInfo(name = "product") var product: String
) : Parcelable

