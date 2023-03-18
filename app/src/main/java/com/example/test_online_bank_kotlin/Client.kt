package org.example

import org.json.JSONObject
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


internal class ClientSomthing(private val addr: String, private val port: Int)
{
    private var socket: Socket? = null
    private var `in` // поток чтения из сокета
            : BufferedReader? = null
    private var out // поток чтения в сокет
            : BufferedWriter? = null

    private var jsonObj : JSONObject? = null

    private var connected : Boolean = false

    var publicKey: PublicKey? = null
    var publicKey_server: PublicKey? = null

    var privateKey: PrivateKey? = null


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

        val generator = KeyPairGenerator.getInstance("RSA")
        val pair = generator.generateKeyPair()

        this.privateKey = pair.private
        this.publicKey = pair.public

        this.send_key()
        this.read_key()

    }



    /**
     * закрытие сокета
     */
    public fun downService() {
        try {
            val jsonObj_out = JSONObject()
            jsonObj_out!!.put("OPERATION","-1")//TODO: операция номер -1 - разрыв соединения
            this.send(jsonObj_out.toString())

            if (!socket!!.isClosed) {
                `in`!!.close()
                out!!.close()
                socket!!.close()
            }
        } catch (ignored: IOException) {
        }
    }


        public fun readMsg() : JSONObject? {
            var str: String?
            try {
                str = `in`!!.readLine() // ждем сообщения с сервера
                val decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")//строгое соблюдение
                decryptCipher.init(Cipher.DECRYPT_MODE, privateKey)
                val decryptedMessageBytes = decryptCipher.doFinal(Base64.getDecoder().decode(str))
                val decryptedMessage = String(decryptedMessageBytes)
                jsonObj = JSONObject(decryptedMessage.toString())
            } catch (e: IOException) {
                downService()
            }

            this.downService()
            return jsonObj
        }



    public fun send(userWord: String)
    {
        try {
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")//строгое соблюдение
            cipher.init(Cipher.ENCRYPT_MODE, publicKey_server)
            out!!.write(Base64.getEncoder().encodeToString(cipher.doFinal(userWord.toByteArray())) + "\n") // отправляем на сервер
            out!!.flush() // чистим
        } catch (e: IOException) {
            downService() // в случае исключения тоже харакири
        }
    }

    public fun send_key()
    {
        try {
            out!!.write(Base64.getEncoder().encodeToString(publicKey!!.getEncoded()) + "\n") // отправляем на сервер
            out!!.flush() // чистим
        } catch (e: IOException) {
            downService() // в случае исключения тоже харакири
        }
    }

    public fun read_key()
    {
        var str: String?
        try {
            str = `in`!!.readLine() // ждем сообщения с сервера
            val data: ByteArray = Base64.getDecoder().decode(str.toByteArray())
            val spec = X509EncodedKeySpec(data)
            val fact = KeyFactory.getInstance("RSA")
            publicKey_server = fact.generatePublic(spec)
        } catch (e: IOException) {
            downService()
        }

    }

    public fun socket_status() : Boolean
    {
        return this.connected
    }

}
