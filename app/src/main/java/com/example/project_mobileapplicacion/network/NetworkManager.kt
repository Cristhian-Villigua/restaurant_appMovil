package com.example.project_mobileapplicacion.network

import android.content.Context
import android.net.wifi.WifiManager
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException

object NetworkManager {

    fun isConnectedToRestaurantNetwork(context: Context, restaurantIpRange: String): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress = wifiManager.connectionInfo.ipAddress
        val ipString = intToIp(ipAddress)
        return ipString.startsWith(restaurantIpRange)
    }

    private fun intToIp(ipAddress: Int): String {
        return try {
            val ipBytes = BigInteger.valueOf(ipAddress.toLong()).toByteArray().reversedArray()
            InetAddress.getByAddress(ipBytes).hostAddress
        } catch (ex: UnknownHostException) {
            "0.0.0.0"
        }
    }
}