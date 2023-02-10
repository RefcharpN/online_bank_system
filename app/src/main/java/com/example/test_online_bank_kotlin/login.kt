package com.example.test_online_bank_kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.ClientSomthing
import org.json.JSONObject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class login : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    System.out.println("Back button clicks")
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val login_edit = view.findViewById<EditText>(R.id.editTextPhone)
        val password_edit = view.findViewById<EditText>(R.id.editTextTextPassword)

        view.findViewById<Button>(R.id.button2).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch{
                val client = ClientSomthing("192.168.102.216",8080)
                //уведомление: идёт подключение к серверу -- через тост
                if (client.socket_status())
                    {
                        val json = JSONObject()
                        json.put("OPERATION", "1")
                        json.put("LOGIN", "${login_edit.text}")
                        json.put("PASSWORD", "${password_edit.text}")
                        client.send(json.toString())

                        val json_input = client.readMsg()
                        System.out.println(json_input)
                        if(json_input!!["EXIST"] != "0")
                        {
                            getActivity()?.runOnUiThread( Runnable{ Navigation.findNavController(view).navigate(R.id.action_login_to_registration_set_pin)})
                        }
                        else
                        {
                            System.out.println("неправильные данные")
                        }

                    }
                else
                    {

                    }

        }
        }
        return view;
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}