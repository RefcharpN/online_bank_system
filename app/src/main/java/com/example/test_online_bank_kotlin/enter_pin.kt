package com.example.test_online_bank_kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.ClientSomthing
import org.json.JSONObject
import java.util.concurrent.Executor

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class enter_pin : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var fragment_view: View? = null

    private var text_field: TextView? = null

    private var btn_01: Button? = null
    private var btn_02: Button? = null
    private var btn_03: Button? = null
    private var btn_04: Button? = null
    private var btn_05: Button? = null
    private var btn_06: Button? = null
    private var btn_07: Button? = null
    private var btn_08: Button? = null
    private var btn_09: Button? = null
    private var btn_00: Button? = null
    private var btn_fng: ImageView? = null
    private var btn_bcksp: ImageView? = null

    private var vw_00: View? = null
    private var vw_01: View? = null
    private var vw_02: View? = null
    private var vw_03: View? = null

    private var passCode_safe: String = ""
    private var numbers_list: ArrayList<String> = ArrayList()

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragment_view = inflater.inflate(R.layout.fragment_enter_pin, container, false)

        init_buttons()
        init_views()
        init_finger()

        return fragment_view
    }

    private fun init_finger()
    {
        executor = ContextCompat.getMainExecutor(fragment_view!!.context)
        biometricPrompt = androidx.biometric.BiometricPrompt(this@enter_pin, executor, object:androidx.biometric.BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                chech_server()
            }
        })
        promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("биометрическая аутентификация").setSubtitle("войдите по лицу или отпечатку").setNegativeButtonText("cansel").build()
    }

    private fun init_buttons()
    {

        this.btn_00 = fragment_view!!.findViewById<Button>(R.id.btn_00)
        this.btn_01 = fragment_view!!.findViewById<Button>(R.id.btn_01)
        this.btn_02 = fragment_view!!.findViewById<Button>(R.id.btn_02)
        this.btn_03 = fragment_view!!.findViewById<Button>(R.id.btn_03)
        this.btn_04 = fragment_view!!.findViewById<Button>(R.id.btn_04)
        this.btn_05 = fragment_view!!.findViewById<Button>(R.id.btn_05)
        this.btn_06 = fragment_view!!.findViewById<Button>(R.id.btn_06)
        this.btn_07 = fragment_view!!.findViewById<Button>(R.id.btn_07)
        this.btn_08 = fragment_view!!.findViewById<Button>(R.id.btn_08)
        this.btn_09 = fragment_view!!.findViewById<Button>(R.id.btn_09)
        this.btn_fng = fragment_view!!.findViewById<ImageView>(R.id.btn_fng)
        this.btn_bcksp = fragment_view!!.findViewById<ImageView>(R.id.btn_bcksp)

        this.btn_00!!.setOnClickListener(this::onClick)
        this.btn_01!!.setOnClickListener(this::onClick)
        this.btn_02!!.setOnClickListener(this::onClick)
        this.btn_03!!.setOnClickListener(this::onClick)
        this.btn_04!!.setOnClickListener(this::onClick)
        this.btn_05!!.setOnClickListener(this::onClick)
        this.btn_06!!.setOnClickListener(this::onClick)
        this.btn_07!!.setOnClickListener(this::onClick)
        this.btn_08!!.setOnClickListener(this::onClick)
        this.btn_09!!.setOnClickListener(this::onClick)
        this.btn_fng!!.setOnClickListener(this::onClick)
        this.btn_bcksp!!.setOnClickListener(this::onClick)
    }

    private fun init_views()
    {
        this.text_field = fragment_view!!.findViewById<TextView>(R.id.textView5)

        this.vw_00 = fragment_view!!.findViewById<View>(R.id.vw_00)
        this.vw_01 = fragment_view!!.findViewById<View>(R.id.vw_01)
        this.vw_02 = fragment_view!!.findViewById<View>(R.id.vw_02)
        this.vw_03 = fragment_view!!.findViewById<View>(R.id.vw_03)
    }

    private fun onClick(view: View)
    {
        when (view.id) {
            R.id.btn_00 -> {
                passNumber("0")
            }

            R.id.btn_01 -> {
                passNumber("1")
            }

            R.id.btn_02 -> {
                passNumber("2")
            }

            R.id.btn_03 -> {
                passNumber("3")
            }

            R.id.btn_04 -> {
                passNumber("4")
            }

            R.id.btn_05 -> {
                passNumber("5")
            }

            R.id.btn_06 -> {
                passNumber("6")
            }

            R.id.btn_07 -> {
                passNumber("7")
            }

            R.id.btn_08 -> {
                passNumber("8")
            }

            R.id.btn_09 -> {
                passNumber("9")
            }

            R.id.btn_fng -> {
                biometricPrompt.authenticate(promptInfo)
            }

            R.id.btn_bcksp -> {
                delete_char()
            }
        }
    }

    private fun passNumber(number: String) {

        if (this.numbers_list.size < 4)
        {
            numbers_list.add(number)
            when (this.numbers_list.size) {
                1 -> {
                    this.vw_00!!.setBackgroundResource(R.drawable.ic_dot_filled)
                }

                2 -> {
                    this.vw_01!!.setBackgroundResource(R.drawable.ic_dot_filled)
                }

                3 -> {
                    this.vw_02!!.setBackgroundResource(R.drawable.ic_dot_filled)
                }

                4 -> {
                    this.vw_03!!.setBackgroundResource(R.drawable.ic_dot_filled)
                    check_pin()
                }
            }
        }
    }

    private fun delete_char()
    {
        if(numbers_list.size > 0)
        {
            when (this.numbers_list.size)
            {
                1 -> {this.vw_00!!.setBackgroundResource(R.drawable.ic_dot_empty)}
                2 -> {this.vw_01!!.setBackgroundResource(R.drawable.ic_dot_empty)}
                3 -> {this.vw_02!!.setBackgroundResource(R.drawable.ic_dot_empty)}
                4 -> {this.vw_03!!.setBackgroundResource(R.drawable.ic_dot_empty)}

            }
            numbers_list.removeAt(numbers_list.size-1)
        }
    }

    private fun check_pin()
    {
        val store = BankDataStore(requireContext())

        this.passCode_safe = this.numbers_list.joinToString("")
        this.numbers_list.clear()

        this.vw_00!!.setBackgroundResource(R.drawable.ic_dot_empty)
        this.vw_01!!.setBackgroundResource(R.drawable.ic_dot_empty)
        this.vw_02!!.setBackgroundResource(R.drawable.ic_dot_empty)
        this.vw_03!!.setBackgroundResource(R.drawable.ic_dot_empty)

        lifecycleScope.launch()
        {
            if(store.get_value("pin_code") == passCode_safe)
            {
                chech_server()
            }
            else
            {
                getActivity()?.runOnUiThread(Runnable {fragment_view!!.findViewById<TextView>(R.id.textView5).text = "неправильный код доступа"})
            }
        }

    }

    private fun chech_server()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val client = ClientSomthing(getString(R.string.server_ip), 8080)
            if (client.socket_status()) {
                val json = JSONObject()

                val store = BankDataStore(requireContext())
                json.put("OPERATION", "1")//TODO: операция номер 1 - проверка логина и пароля
                json.put("LOGIN", "${store.get_value("phone")}")
                json.put("PASSWORD", "${store.get_value("password")}")

                client.send(json.toString())

                val json_input = client.readMsg()

                if (json_input!!["EXIST"] != "0") {
                    getActivity()?.runOnUiThread(Runnable {zaplatka()})
                }
                else
                {
                    getActivity()?.runOnUiThread(Runnable {
                        Snackbar.make(fragment_view!!, "не удалось пройти аутентификацию. перезайдите в аккаунт", Snackbar.LENGTH_SHORT).show()})
                }
            }
            else
            {
                getActivity()?.runOnUiThread(Runnable {Snackbar.make(fragment_view!!, "неудачное подключение к серверу.", Snackbar.LENGTH_SHORT).show()})
            }
        }
    }

    private fun zaplatka()
    {
        fragment_view?.let { Navigation.findNavController(it).navigate(R.id.action_enter_pin_to_main_page) }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            enter_pin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}