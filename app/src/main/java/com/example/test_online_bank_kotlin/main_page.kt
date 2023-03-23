package com.example.test_online_bank_kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class main_page : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var fragment_card: card? = null
    var fragment_chat: chat? = null
    var fragment_person: person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        fragment_card = card()
        fragment_chat = chat()
        fragment_person = person()

        fragmentManager!!.beginTransaction().replace(R.id.main_frame, fragment_card!!).commit()

    }


    lateinit var bottomNav : BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_page, container, false)
        val fragmentManager = fragmentManager

        bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.card -> {
                    fragmentManager!!.beginTransaction().replace(R.id.main_frame, fragment_card!!).commit()
                    true
                }
                R.id.chat -> {
                    fragmentManager!!.beginTransaction().replace(R.id.main_frame, fragment_chat!!).commit()
                    true
                }
                R.id.person -> {
                    fragmentManager!!.beginTransaction().replace(R.id.main_frame, fragment_person!!).commit()
                    true
                }
            }
            true
        }


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            main_page().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}