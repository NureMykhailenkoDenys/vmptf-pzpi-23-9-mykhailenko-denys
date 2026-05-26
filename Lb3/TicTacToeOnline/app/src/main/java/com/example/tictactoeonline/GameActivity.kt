package com.example.tictactoeonline

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class GameActivity : AppCompatActivity() {

    private lateinit var gameId: String

    private lateinit var nickname: String

    private lateinit var statusText: TextView

    private lateinit var boardLayout: GridLayout

    private lateinit var buttons: Array<Button>

    private lateinit var backButton: Button

    private var canMove = false

    private var gameEnded = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_game
        )

        gameId =
            intent.getStringExtra(
                "gameId"
            ) ?: ""

        nickname =
            intent.getStringExtra(
                "nickname"
            ) ?: ""

        statusText =
            findViewById(R.id.statusText)

        boardLayout =
            findViewById(R.id.boardLayout)

        backButton =
            findViewById(R.id.backButton)

        buttons =
            Array(9) {
                Button(this)
            }

        createBoard()

        val socket =
            SocketHandler.getSocket()

        statusText.text =
            "Очікування другого гравця"

        socket.on(
            "startGame"
        ) {

            runOnUiThread {

                statusText.text =
                    "Гру розпочато"
            }
        }

        socket.on(
            "updateGame"
        ) { args ->

            runOnUiThread {

                val data =
                    args[0] as JSONObject

                val game =
                    data.getJSONObject("game")

                val board =
                    game.getJSONArray("board")

                val players =
                    game.getJSONArray(
                        "players"
                    )

                val currentPlayer =
                    game.getInt(
                        "currentPlayer"
                    )

                val winner =
                    if (
                        game.isNull("winner")
                    ) {
                        ""
                    } else {
                        game.getString("winner")
                    }

                for (i in 0 until 9) {

                    buttons[i].text =
                        board.getString(i)
                }

                if (
                    players.length() < 2
                ) {

                    statusText.text =
                        "Очікування другого гравця"

                    return@runOnUiThread
                }

                if (
                    winner.isNotEmpty()
                ) {

                    gameEnded = true

                    statusText.text =
                        "Переможець: $winner"

                    backButton.visibility =
                        View.VISIBLE

                    backButton.setOnClickListener {

                        val leaveData =
                            JSONObject()

                        leaveData.put(
                            "gameId",
                            gameId
                        )

                        socket.emit(
                            "leaveGame",
                            leaveData
                        )

                        finish()
                    }

                    return@runOnUiThread
                }

                val currentNickname =
                    players
                        .getJSONObject(
                            currentPlayer
                        )
                        .getString(
                            "nickname"
                        )

                canMove =
                    currentNickname == nickname

                statusText.text =
                    "Хід: $currentNickname"
            }
        }
    }

    private fun createBoard() {

        val socket =
            SocketHandler.getSocket()

        for (i in 0 until 9) {

            val button =
                Button(this)

            button.textSize = 32f

            button.width = 220

            button.height = 220

            button.setOnClickListener {

                if (!canMove) {

                    return@setOnClickListener
                }

                if (gameEnded) {

                    return@setOnClickListener
                }

                if (
                    statusText.text ==
                    "Очікування другого гравця"
                ) {

                    return@setOnClickListener
                }

                if (
                    button.text.isNotEmpty()
                ) {

                    return@setOnClickListener
                }

                val data =
                    JSONObject()

                data.put(
                    "gameId",
                    gameId
                )

                data.put(
                    "index",
                    i
                )

                socket.emit(
                    "makeMove",
                    data
                )
            }

            buttons[i] =
                button

            boardLayout.addView(
                button
            )
        }
    }

    override fun onDestroy() {

        super.onDestroy()

        val socket =
            SocketHandler.getSocket()

        socket.off("updateGame")

        socket.off("startGame")
    }
}