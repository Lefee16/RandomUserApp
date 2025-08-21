package com.example.randomuserapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuserapp.R
import com.example.randomuserapp.repository.UserRepositoryImpl
import com.example.randomuserapp.retrofit.RetrofitInstance
import com.example.randomuserapp.room.UserEntity
import com.example.randomuserapp.viewmodel.UserViewModel
import com.example.randomuserapp.viewmodel.UserViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var blockingOverlay: View
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private val userList = mutableListOf<UserEntity>()
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepositoryImpl(applicationContext, Dispatchers.IO))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        blockingOverlay = findViewById(R.id.blockingOverlay)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.rcView)
        adapter = UserAdapter(userList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val buttonAddUser: Button = findViewById(R.id.buttonAdd)
        buttonAddUser.setOnClickListener {
            addUserFromApi()
        }

        val buttonUpdateUsers: Button = findViewById(R.id.buttonUpdate)
        buttonUpdateUsers.setOnClickListener {
            updateUsersFromApi()
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            @Suppress("DEPRECATION")
            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                val position = viewHolder.adapterPosition
                val user = userList[position]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        userViewModel.deleteUser(user)
                    }
                    ItemTouchHelper.RIGHT -> {
                        updateSingleUser(user)
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        userViewModel.dataloading.observe(this) { isLoading ->
            buttonAddUser.isEnabled = !isLoading
            buttonUpdateUsers.isEnabled = !isLoading
            recyclerView.isEnabled = !isLoading
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            blockingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        userViewModel.getAllUsers().observe(this){ usersFromDB ->
            adapter.updateList(usersFromDB)
        }
    }

    private fun updateUsersFromApi() {
        lifecycleScope.launch(Dispatchers.IO) {
            userViewModel.setLoading(true)
            try {
                val currentUsers = userViewModel.getAllUsersList()
                if (currentUsers.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Нет пользователей для обновления",
                            Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                val updatedList = currentUsers.map { localUser ->
                    val apiResponse = RetrofitInstance.api.getRandomUser()
                    val results = apiResponse.results
                    if (results.isNullOrEmpty()) {
                        localUser
                    } else {
                        val apiUser = results[0]
                        localUser.copy(
                            name = "${apiUser.name.title} ${apiUser.name.first} ${apiUser.name.last}",
                            phone = apiUser.phone,
                            location = "${apiUser.location.street.number} ${apiUser.location.street.name}, " +
                                    "${apiUser.location.city}, ${apiUser.location.state}, ${apiUser.location.country}",
                            picture = apiUser.picture.large
                        )
                    }
                }
                userViewModel.updateUsers(updatedList)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Список пользователей обновлён",
                        Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Ошибка обновления: ${e.message}",
                        Toast.LENGTH_SHORT).show()
                }
            } finally {
                userViewModel.setLoading(false)
            }
        }
    }

    private fun addUserFromApi() {
        lifecycleScope.launch(Dispatchers.IO) {
            userViewModel.setLoading(true)
            try {
                val newUser = RetrofitInstance.api.getRandomUser().results[0]
                val entity = UserEntity(
                    name = "${newUser.name.title} ${newUser.name.first} ${newUser.name.last}",
                    phone = newUser.phone,
                    location = "${newUser.location.street.number} ${newUser.location.street.name}, ${newUser.location.city}, ${newUser.location.state}, ${newUser.location.country}",
                    picture = newUser.picture.large
                )
                userViewModel.insertUsers(entity)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Ошибка загрузки: ${e.message}",
                        Toast.LENGTH_SHORT).show()
                }
            } finally {
                userViewModel.setLoading(false)
            }
        }
    }

    private fun updateSingleUser(user: UserEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiUser = RetrofitInstance.api.getRandomUser().results[0]
                val updatedUser = user.copy(
                    name = "${apiUser.name.title} ${apiUser.name.first} ${apiUser.name.last}",
                    phone = apiUser.phone,
                    location = "${apiUser.location.street.number} ${apiUser.location.street.name}, " +
                            "${apiUser.location.city}, ${apiUser.location.state}, ${apiUser.location.country}",
                    picture = apiUser.picture.large
                )
                userViewModel.updateUser(updatedUser)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Ошибка обновления пользователя: ${e.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}