package com.example.test_online_bank_kotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.ClientSomthing
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class registration_phone : Fragment() {
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

        val view = inflater.inflate(R.layout.fragment_registration_phone, container, false)

        val phone_edit = view.findViewById<TextInputEditText>(R.id.textPhone)
        val password_edit = view.findViewById<TextInputEditText>(R.id.textPasword)
        val phone_field = view.findViewById<TextInputLayout>(R.id.textInputPhone)

        phone_field.helperText = ""

        view.findViewById<Button>(R.id.button).setOnClickListener {
            view.findViewById<Button>(R.id.button).isClickable = false

            if(phone_edit.text!!.isEmpty() || password_edit.text!!.isEmpty())
            {
                phone_field.helperText = "заполните все поля"
            }
            else
            {
                CoroutineScope(Dispatchers.IO).launch {
                    val client = ClientSomthing(getString(R.string.server_ip), 8080)
                    if (client.socket_status()) {
                        val json = JSONObject()

                        json.put("OPERATION", "2")//TODO: операция номер 2 - проверка телефона
                        json.put("PHONE", "${phone_edit.text}")

                        client.send(json.toString())

                        val json_input = client.readMsg()

                        if(json_input!!["EXIST"] == "0")
                        {
                            getActivity()?.runOnUiThread( Runnable{ Navigation.findNavController(view).navigate(R.id.action_registration_phone_to_registration_personal_data)})
                        }
                        else
                        {
                            getActivity()?.runOnUiThread( Runnable{phone_field.helperText = getString(R.string.exist_phone)});
                        }
                    }
                }
            }

            view.findViewById<Button>(R.id.button).isClickable = true
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            registration_phone().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}