package com.example.test_online_bank_kotlin

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import io.bitfactory.pincodelayout.PinCodeActions
import io.bitfactory.pincodelayout.PinCodeLayout

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class registration_set_pin : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var fragment_view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    private val callback: PinCodeActions = object : PinCodeActions {
        @RequiresApi(Build.VERSION_CODES.S)
        override fun onPinEntered(pin: String) {
            // Called when the pin is fully entered. Returns the pin1
            if( fragment_view!!.findViewById<TextView>(R.id.textView5).text.equals("повторно введите пин-код"))
            {

            }
            else
            {
                Navigation.findNavController(fragment_view!!).navigate(R.id.action_registration_set_pin_self)
            }
        }

        override fun onPinCleared() {
            // Called when the pin is cleared/empty
        }

        override fun onPinFilled() {
            // Called when the pin is entered and the View looses focus

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragment_view = inflater.inflate(R.layout.fragment_registration_set_pin, container, false)

        fragment_view!!.findViewById<PinCodeLayout>(R.id.pinCodeLayout).setCallback(callback)


        fragment_view!!.findViewById<Button>(R.id.button).setOnClickListener {
            Navigation.findNavController(fragment_view!!).navigate(R.id.action_registration_set_pin_to_main_page)
        }

        return fragment_view
    }





    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            registration_set_pin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}