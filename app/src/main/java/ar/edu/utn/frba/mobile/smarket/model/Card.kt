package ar.edu.utn.frba.mobile.smarket.model

import com.google.android.material.textfield.TextInputEditText

class Card() {

    lateinit var number : String
    lateinit var year: String
    lateinit var month: String
    lateinit var titular: String
    var priority = 0

    constructor(
        number: TextInputEditText,
        year: TextInputEditText,
        month: TextInputEditText,
        titular: TextInputEditText
    ) : this() {
        this.number = number.text.toString()
        this.year = year.text.toString()
        this.month = month.text.toString()
        this.titular = titular.text.toString()
    }
}
