package com.example.randomuserapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "users")
data class UserEntity(

    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo (name = "Name")
    val name: String,

    @ColumnInfo (name = "Phone")
    val phone: String,

    @ColumnInfo (name = "Location")
    val location: String,

    @ColumnInfo (name = "Picture")
    val picture: String,
)