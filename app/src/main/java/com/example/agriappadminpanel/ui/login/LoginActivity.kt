package com.example.agriappadminpanel.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.agriappadminpanel.MainActivity
import com.example.agriappadminpanel.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.login.setOnClickListener {

            if (binding.username.text.toString() == "Admin" && binding.password.text.toString() == "Admin")
            {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
            else
            {
                Toast.makeText(this, "Enter Correct Credentials !!", Toast.LENGTH_LONG).show()

            }

        }
    }
}

