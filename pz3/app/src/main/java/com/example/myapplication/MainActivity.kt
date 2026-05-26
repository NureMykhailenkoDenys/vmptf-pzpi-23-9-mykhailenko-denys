package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val monthInput = findViewById<EditText>(R.id.monthInput)
        val showButton = findViewById<Button>(R.id.showButton)
        val resultText = findViewById<TextView>(R.id.resultText)

        showButton.setOnClickListener {

            val month = monthInput.text.toString().toIntOrNull()

            val holidays = when(month) {
                1 -> "Новий рік, Різдво"
                2 -> "День святого Валентина"
                3 -> "Міжнародний жіночий день"
                4 -> "Великдень"
                5 -> "День праці"
                6 -> "День Конституції України"
                7 -> "Івана Купала"
                8 -> "День Незалежності України"
                9 -> "День знань"
                10 -> "День захисників України"
                11 -> "Осінні свята"
                12 -> "Святий Миколай, Новий рік"
                else -> "Невірний номер місяця"
            }

            resultText.text = holidays
        }
    }
}