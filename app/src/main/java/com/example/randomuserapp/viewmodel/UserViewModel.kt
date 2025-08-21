package com.example.randomuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.randomuserapp.repository.UserRepository
import com.example.randomuserapp.room.UserEntity
import kotlinx.coroutines.launch

class UserViewModel (private val repository: UserRepository): ViewModel(){

    private val _dataloading = MutableLiveData<Boolean>()
    val dataloading: LiveData<Boolean> get() = _dataloading

    fun setLoading(isLoading: Boolean) {
        _dataloading.postValue(isLoading)
    }

    fun insertUsers(users: UserEntity){
        viewModelScope.launch {
            repository.insertAllUsers(users)
        }
    }

    fun updateUsers(users: List<UserEntity>){
        viewModelScope.launch {
            _dataloading.postValue(true)
            repository.updateAllUsers(users)
            _dataloading.postValue(false)
            repository.updateAllUsers(users)
        }
    }

    fun getAllUsers(): LiveData<List<UserEntity>> {
        _dataloading.postValue(true)
        val usersLiveData = repository.getAllUsers().asLiveData()
        _dataloading.postValue(false)
        return usersLiveData
    }

    suspend fun getAllUsersList(): List<UserEntity> {
        return repository.getAllUsersList()
    }

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }

    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }
}