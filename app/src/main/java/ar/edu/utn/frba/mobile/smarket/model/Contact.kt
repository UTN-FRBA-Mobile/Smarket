package ar.edu.utn.frba.mobile.smarket.model

import com.google.android.material.textfield.TextInputEditText

class Contact() {

    lateinit var name: String
    lateinit var number: String

    constructor(name: TextInputEditText, number: TextInputEditText)
            : this() {
        this.name = name.text.toString()
        this.number = number.text.toString()
    }
}