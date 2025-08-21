package com.example.randomuserapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1)

abstract class UserDb: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        fun createDatabase(context: Context): UserDb{
            return Room.databaseBuilder(
                context,
                UserDb::class.java,
                "DB"
            ).build()
        }
    }
}