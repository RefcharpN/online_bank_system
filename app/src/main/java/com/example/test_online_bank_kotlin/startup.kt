package com.example.test_online_bank_kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.example.ClientSomthing

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class startup : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_startup, container, false)
        //TODO: запомнить состояние входа
        val store = BankDataStore(requireContext())

        CoroutineScope(IO).launch{
            val client = ClientSomthing(getString(R.string.server_ip),8080)
            //уведомление: идёт подключение к серверу -- через тост
            if (client.socket_status())
            {
                client.downService()
                //TODO: запомнить состояние входа
                val store = BankDataStore(requireContext())
                if (store.get_value("phone") != "null")
                {
                    getActivity()?.runOnUiThread( Runnable{Navigation.findNavController(view).navigate(R.id.action_startup_to_enter_pin)})
                }
                else
                {
                    getActivity()?.runOnUiThread( Runnable{Navigation.findNavController(view).navigate(R.id.action_startup_to_login)})
                }

            }
            else
            {
                getActivity()?.runOnUiThread( Runnable{view.findViewById<TextView>(R.id.textView).setText("неудачное подключение") })
            }

        }

        return view;
    }



    companion object {
        fun newInstance(param1: String, param2: String) =
            startup().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}