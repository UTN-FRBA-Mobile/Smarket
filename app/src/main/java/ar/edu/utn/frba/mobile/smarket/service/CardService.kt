package ar.edu.utn.frba.mobile.smarket.service

import android.content.Context
import androidx.collection.ArraySet
import androidx.preference.PreferenceManager
import ar.edu.utn.frba.mobile.smarket.model.Card
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type
import java.util.ArrayList

object CardService {

    private val LONG_SECURITY_CODE_VISA = 3
    private val LONG_SECURITY_CODE_MASTERCARD = 3
    private val LONG_SECURITY_CODE_AMERICAN_EXPRESS = 4

    private val START_AMERICAN_EXPRESS = 3
    private val START_VISA = 4
    private val START_MASTERCARD = 5

    private val PREFERENCE_KEY = "CARDS"
    private val gson = Gson()

    fun logMaxSecurityCode(cardNumber: String): Int {
        return when (cardNumber[0].toInt()) {
            START_AMERICAN_EXPRESS -> LONG_SECURITY_CODE_AMERICAN_EXPRESS
            START_VISA -> LONG_SECURITY_CODE_VISA
            START_MASTERCARD -> LONG_SECURITY_CODE_MASTERCARD
            else -> 3
        }
    }

    fun save(card: Card, context: Context) {
        var cards = this.get(context)
        val actualCard = cards.firstOrNull { c: Card -> card.number != c.number }

        if (actualCard == null || actualCard.priority > 0) {
            if (actualCard == null) {
                cards.forEach { it.priority += 1 }
                cards = cards + card
            } else {
                actualCard.priority = -1
                var position = 0
                cards.sortedBy { it.priority }.forEach {
                    it.priority = position
                    position++
                }
            }
            val stringCards = cards.map { gson.toJson(it) }.toSet()
            val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
            preferenceManager.edit().putStringSet(PREFERENCE_KEY, stringCards).apply()
        }

    }

    fun get(context: Context): List<Card> {
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
        val stringCards = preferenceManager.getStringSet(PREFERENCE_KEY, ArraySet())!!
        return stringCards.map { stringCard -> gson.fromJson(stringCard, Card::class.java) }
    }

}
