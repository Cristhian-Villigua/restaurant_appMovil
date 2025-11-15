package com.example.project_mobileapplicacion.websocket

import android.os.Handler
import android.os.Looper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketManager(private val listener: WebSocketListener) {

    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    fun connect(serverUrl: String) {
        val request = Request.Builder().url(serverUrl).build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        if (::webSocket.isInitialized) {
            webSocket.send(message)
        }
    }

    fun close() {
        if (::webSocket.isInitialized) {
            webSocket.close(1000, "Closing")
        }
    }
}
class MyWebSocketListener : WebSocketListener() {

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("WebSocket conectado")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        mainHandler.post {
            println("Mensaje recibido: $text")
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        t.printStackTrace()
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        println("WebSocket cerrado: $reason")
    }
}
class KitchenWebSocketListener(
    private val onMessageReceived: (String) -> Unit
) : WebSocketListener() {

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("WebSocket cocina conectado")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        mainHandler.post {
            onMessageReceived(text)
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        t.printStackTrace()
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        println("WebSocket cocina cerrado: $reason")
    }
}

