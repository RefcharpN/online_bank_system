package com.example.test_online_bank_kotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.ClientSomthing
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class registration_personal_data : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    val args: registration_personal_dataArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_registration_personal_data, container, false)

        val user_phone_pass = args.userPhonePass

        val fname_edit = view.findViewById<TextInputEditText>(R.id.fname)
        val lname_edit = view.findViewById<TextInputEditText>(R.id.lname)
        val mname_edit = view.findViewById<TextInputEditText>(R.id.mname)
        val series_edit = view.findViewById<TextInputEditText>(R.id.series)
        val number_edit = view.findViewById<TextInputEditText>(R.id.number)
        val department_edit = view.findViewById<TextInputEditText>(R.id.department)
        val date_edit = view.findViewById<TextInputEditText>(R.id.date_given)



        view.findViewById<Button>(R.id.button3).setOnClickListener {
            view.findViewById<Button>(R.id.button3).isClickable = false

            if(lname_edit.text!!.isEmpty() || fname_edit.text!!.isEmpty() || mname_edit.text!!.isEmpty() || series_edit.text!!.isEmpty() || number_edit.text!!.isEmpty() || department_edit.text!!.isEmpty() || date_edit.text!!.isEmpty())
            {
                getActivity()?.runOnUiThread(Runnable { Snackbar.make(view, "заполните все поля", Snackbar.LENGTH_SHORT).show()})
            }
            else
            {
                CoroutineScope(Dispatchers.IO).launch {
                    val client = ClientSomthing(getString(R.string.server_ip), getString(R.string.server_port).toInt())
                    if (client.socket_status()) {
                        val json = JSONObject()

                        json.put("OPERATION", "3")//TODO: операция номер 3 - регистрация

                        json.put("phone", user_phone_pass[0])
                        json.put("password", user_phone_pass[1])
                        json.put("fname", fname_edit.text.toString())
                        json.put("lname", lname_edit.text.toString())
                        json.put("mname", mname_edit.text.toString())
                        json.put("series", series_edit.text.toString())
                        json.put("number", number_edit.text.toString())
                        json.put("department", department_edit.text.toString())
                        json.put("date", date_edit.text.toString())

                        client.send(json.toString())

                        val json_input = client.readMsg()

                        if(json_input!!["register_status"] == "0")
                        {
                            val action = registration_personal_dataDirections.actionRegistrationPersonalDataToRegistrationSetPin(arrayOf<String>(user_phone_pass[0], user_phone_pass[1]))
                            getActivity()?.runOnUiThread( Runnable{ Navigation.findNavController(view).navigate(action)})
                        }
                        else
                        {
                            //getActivity()?.runOnUiThread( Runnable{phone_field.helperText = getString(R.string.exist_phone)});
                        }
                    }
                }
            }

            view.findViewById<Button>(R.id.button3).isClickable = true
        }



        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            registration_personal_data().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}