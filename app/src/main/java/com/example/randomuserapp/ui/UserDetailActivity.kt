package com.example.randomuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.randomuserapp.R

class UserDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = intent.getStringExtra("name")
        val location = intent.getStringExtra("location")
        val phone = intent.getStringExtra("phone")
        val picture = intent.getStringExtra("picture")

        val textViewName = findViewById<TextView>(R.id.textViewName)
        val textViewPhone = findViewById<TextView>(R.id.textViewPhone)
        val textViewLocation = findViewById<TextView>(R.id.textViewLocation)
        val imageView = findViewById<ImageView>(R.id.imageView)

        textViewName.text = name
        textViewPhone.text = phone
        textViewLocation.text = location
        Glide
            .with(this)
            .load(picture)
            .into(imageView)

        textViewPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, ("tel:$phone").toUri())
            startActivity(intent)
        }

        textViewLocation.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, ("geo:0,0?q=$location").toUri())
            startActivity(intent)
        }
    }
}