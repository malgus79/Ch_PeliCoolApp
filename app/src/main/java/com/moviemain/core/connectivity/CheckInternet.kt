package com.moviemain.core.connectivity

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object CheckInternet {

    suspend fun isNetworkAvailable() = coroutineScope {
        return@coroutineScope try {
            val sock = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)
            withContext(Dispatchers.IO) {
                sock.connect(socketAddress, 1000)
            }
            withContext(Dispatchers.IO) {
                sock.close()
            }
            true
        } catch (e: IOException) {
            false
        }
    }
}