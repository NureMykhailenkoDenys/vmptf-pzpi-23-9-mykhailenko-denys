package com.example.tictactoeonline

import io.socket.client.IO
import io.socket.client.Socket

object SocketHandler {

    private lateinit var socket: Socket

    fun setSocket() {

        socket =
            IO.socket(
                "http://192.168.3.87:5000/"
            )
    }

    fun getSocket(): Socket {

        return socket
    }

    fun establishConnection() {

        socket.connect()
    }

    fun closeConnection() {

        socket.disconnect()
    }
}