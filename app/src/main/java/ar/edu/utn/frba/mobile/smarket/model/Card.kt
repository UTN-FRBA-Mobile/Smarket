package ar.edu.utn.frba.mobile.smarket.model

import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import kotlin.random.Random

class Card() {

    fun getType(): CardType {
        return if(number.startsWith("4"))
            CardType.VISA
        else
            CardType.MASTER
    }

    lateinit var number : String
    lateinit var year: String
    lateinit var month: String
    lateinit var titular: String
    val id : Long = Random.nextLong()
    var enable = true
    var priority = 0

    constructor(
        number: AppCompatAutoCompleteTextView,
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
