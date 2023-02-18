package org.example

import android.telecom.Call
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.system.exitProcess


internal class ClientSomthing(private val addr: String, private val port: Int)
{
    private var socket: Socket? = null
    private var `in` // поток чтения из сокета
            : BufferedReader? = null
    private var out // поток чтения в сокет
            : BufferedWriter? = null

    private var jsonObj : JSONObject? = null

    private var connected : Boolean = false


    init {
        if (open_socket())
            open_IO()
    }

    private fun open_socket() : Boolean
    {
        try {
            socket = Socket()
            socket!!.connect(InetSocketAddress(addr, port), 10000)

        } catch (e: IOException) {
            System.err.println("Socket failed")
            return false
        }
        return true
    }

    private fun open_IO()
    {
        try {
            `in` = BufferedReader(InputStreamReader(socket!!.getInputStream()))
            out = BufferedWriter(OutputStreamWriter(socket!!.getOutputStream()))

        } catch (e: IOException) {
            downService()
            return
        }
        connected = true
    }

    /**
     * закрытие сокета
     */
    public fun downService() {
        try {
            val jsonObj_out = JSONObject()
            jsonObj_out!!.put("OPERATION","-1")
            this.send(jsonObj_out.toString())

            if (!socket!!.isClosed) {
                `in`!!.close()
                out!!.close()
                socket!!.close()
            }
        } catch (ignored: IOException) {
        }
    }



    // нить чтения сообщений с сервера

        public fun readMsg() : JSONObject? {
            var str: String?
            try {
                    str = `in`!!.readLine() // ждем сообщения с сервера
                    jsonObj = JSONObject(str)
            } catch (e: IOException) {
                downService()
            }

            this.downService()
            return jsonObj
        }



    public fun send(userWord: String)
    {
        try {
            out!!.write(userWord + "\n") // отправляем на сервер
            out!!.flush() // чистим
        } catch (e: IOException) {
            downService() // в случае исключения тоже харакири
        }
    }

    public fun socket_status() : Boolean
    {
        return this.connected
    }

}
