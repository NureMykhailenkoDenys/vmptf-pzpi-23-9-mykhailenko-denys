package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var cards: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val card1 = findViewById<Button>(R.id.card1)
        val card2 = findViewById<Button>(R.id.card2)
        val card3 = findViewById<Button>(R.id.card3)

        val spinner1 = findViewById<Spinner>(R.id.spinner1)
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val spinner3 = findViewById<Spinner>(R.id.spinner3)

        val checkButton = findViewById<Button>(R.id.checkButton)
        val restartButton = findViewById<Button>(R.id.restartButton)

        val resultText = findViewById<TextView>(R.id.resultText)

        val values = arrayOf("A", "K", "Q")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            values
        )

        spinner1.adapter = adapter
        spinner2.adapter = adapter
        spinner3.adapter = adapter

        generateCards()

        checkButton.setOnClickListener {

            val userAnswers = listOf(
                spinner1.selectedItem.toString(),
                spinner2.selectedItem.toString(),
                spinner3.selectedItem.toString()
            )

            var correct = 0

            val buttons = listOf(card1, card2, card3)

            for (i in cards.indices) {

                if (userAnswers[i] == cards[i]) {

                    buttons[i].setBackgroundColor(
                        resources.getColor(android.R.color.holo_blue_light)
                    )

                    correct++

                } else {

                    buttons[i].setBackgroundColor(
                        resources.getColor(android.R.color.darker_gray)
                    )
                }

                buttons[i].text = cards[i]
                checkButton.visibility = View.GONE
            }

            if (correct > 0) {
                resultText.text = "Ви вгадали $correct карти!"
            } else {
                resultText.text = "Ви не вгадали жодної карти"
            }

            restartButton.visibility = View.VISIBLE
        }

        restartButton.setOnClickListener {

            generateCards()

            resultText.text = ""

            card1.text = "?"
            card2.text = "?"
            card3.text = "?"

            card1.setBackgroundColor(
                resources.getColor(android.R.color.holo_purple)
            )

            card2.setBackgroundColor(
                resources.getColor(android.R.color.holo_purple)
            )

            card3.setBackgroundColor(
                resources.getColor(android.R.color.holo_purple)
            )

            restartButton.visibility = View.GONE
            checkButton.visibility = View.VISIBLE
        }
    }

    private fun generateCards() {

        val values = listOf("A", "K", "Q")

        cards = List(3) {
            values[Random.nextInt(values.size)]
        }
    }
}