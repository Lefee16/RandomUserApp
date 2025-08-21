package com.example.randomuserapp.repository

import android.content.Context
import com.example.randomuserapp.room.UserDao
import com.example.randomuserapp.room.UserDb
import com.example.randomuserapp.room.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    context: Context,
    private val backgroundDispatcher: CoroutineDispatcher
): UserRepository {

    private val userDao: UserDao

    init {
        val database = UserDb.createDatabase(context)
        userDao = database.userDao()
    }

    override fun getAllUsers(): Flow<List<UserEntity>> {
        return userDao.getAllUsers()
    }

    override suspend fun getAllUsersList(): List<UserEntity> =
        withContext(backgroundDispatcher){
            userDao.getAllUsersList()
        }


    override suspend fun insertAllUsers(users: UserEntity) {
        withContext(backgroundDispatcher){
            userDao.insertAllUsers(users)
        }
    }

    override suspend fun updateAllUsers(users: List<UserEntity>) {
        withContext(backgroundDispatcher){
            userDao.updateAllUsers(users)
        }
    }

    override suspend fun updateUser(user: UserEntity) {
        withContext(backgroundDispatcher){
            userDao.updateUser(user)
        }
    }

    override suspend fun deleteUser(user: UserEntity) {
        withContext(backgroundDispatcher){
            userDao.deleteUser(user)
        }
    }
}