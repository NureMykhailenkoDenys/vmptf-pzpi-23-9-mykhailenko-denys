package com.example.tictactoeonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val nicknameInput =
            findViewById<EditText>(
                R.id.nicknameInput
            )

        val loginButton =
            findViewById<Button>(
                R.id.loginButton
            )

        loginButton.setOnClickListener {

            val nickname =
                nicknameInput.text.toString()

            if (nickname.isNotEmpty()) {

                val intent =
                    Intent(
                        this,
                        LobbyActivity::class.java
                    )

                intent.putExtra(
                    "nickname",
                    nickname
                )

                startActivity(intent)
            }
        }
    }
}