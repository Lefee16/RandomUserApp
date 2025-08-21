package com.example.randomuserapp.repository

import com.example.randomuserapp.room.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllUsers(): Flow<List<UserEntity>>

    suspend fun getAllUsersList(): List<UserEntity>

    suspend fun insertAllUsers(users: UserEntity)

    suspend fun updateAllUsers(users: List<UserEntity>)

    suspend fun updateUser(user: UserEntity)

    suspend fun deleteUser(user: UserEntity)
}