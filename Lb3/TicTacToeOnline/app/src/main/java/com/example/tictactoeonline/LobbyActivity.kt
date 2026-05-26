package com.example.tictactoeonline

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class LobbyActivity : AppCompatActivity() {

    private lateinit var gamesListView: ListView

    private lateinit var leaderboardText: TextView

    private lateinit var createGameButton: Button

    private lateinit var nickname: String

    private val gamesList =
        mutableListOf<String>()

    private val gameIds =
        mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_lobby
        )

        nickname =
            intent.getStringExtra(
                "nickname"
            ) ?: "Player"

        gamesListView =
            findViewById(R.id.gamesListView)

        leaderboardText =
            findViewById(R.id.leaderboardText)

        createGameButton =
            findViewById(R.id.createGameButton)

        SocketHandler.setSocket()

        SocketHandler.establishConnection()

        val socket =
            SocketHandler.getSocket()

        createGameButton.setOnClickListener {

            val data =
                JSONObject()

            data.put(
                "nickname",
                nickname
            )

            socket.emit(
                "createGame",
                data
            )
        }

        socket.on(
            "gameCreated"
        ) { args ->

            val data =
                args[0] as JSONObject

            val gameId =
                data.getString("gameId")

            runOnUiThread {

                openGame(
                    gameId
                )
            }
        }

        socket.on(
            "startGame"
        ) { args ->

            val data =
                args[0] as JSONObject

            val game =
                data.getJSONObject(
                    "game"
                )

            val players =
                game.getJSONArray(
                    "players"
                )

            var joined =
                false

            for (i in 0 until players.length()) {

                val player =
                    players.getJSONObject(i)

                if (
                    player.getString(
                        "nickname"
                    ) == nickname
                ) {

                    joined = true
                }
            }

            if (!joined) {
                return@on
            }

            val gameId =
                data.getString(
                    "gameId"
                )

            runOnUiThread {

                openGame(
                    gameId
                )
            }
        }

        socket.on(
            "gamesList"
        ) { args ->

            runOnUiThread {

                gamesList.clear()

                gameIds.clear()

                val array =
                    args[0] as JSONArray

                for (i in 0 until array.length()) {

                    val game =
                        array.getJSONObject(i)

                    val name =
                        game.getString("name")

                    val id =
                        game.getString("id")

                    gamesList.add(name)

                    gameIds.add(id)
                }

                val adapter =
                    ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        gamesList
                    )

                gamesListView.adapter =
                    adapter
            }
        }

        gamesListView.setOnItemClickListener {
                _, _, position, _ ->

            val data =
                JSONObject()

            data.put(
                "gameId",
                gameIds[position]
            )

            data.put(
                "nickname",
                nickname
            )

            socket.emit(
                "joinGame",
                data
            )
        }

        socket.on(
            "updateLeaderboard"
        ) { args ->

            runOnUiThread {

                val data =
                    args[0] as JSONObject

                val builder =
                    StringBuilder()

                val keys =
                    data.keys()

                while (keys.hasNext()) {

                    val key =
                        keys.next()

                    builder.append(
                        "$key : ${data.getInt(key)}\n"
                    )
                }

                leaderboardText.text =
                    builder.toString()
            }
        }
    }

    private fun openGame(
        gameId: String
    ) {

        val intent =
            Intent(
                this,
                GameActivity::class.java
            )

        intent.putExtra(
            "gameId",
            gameId
        )

        intent.putExtra(
            "nickname",
            nickname
        )

        startActivity(intent)
    }

    override fun onDestroy() {

        super.onDestroy()

        val socket =
            SocketHandler.getSocket()

        socket.off("gamesList")

        socket.off("startGame")

        socket.off("gameCreated")

        socket.off("updateLeaderboard")
    }
}