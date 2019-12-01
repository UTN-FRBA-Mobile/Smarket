package ar.edu.utn.frba.mobile.smarket.model

import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import kotlin.random.Random

class Contact() {

    lateinit var name: String
    lateinit var number: String
    val id = Random.nextLong()
    var enable = true

    constructor(name: AppCompatAutoCompleteTextView, number: TextInputEditText)
            : this() {
        this.name = name.text.toString()
        this.number = number.text.toString()
    }
}