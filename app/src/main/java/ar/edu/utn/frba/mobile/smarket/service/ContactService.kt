package ar.edu.utn.frba.mobile.smarket.service

import android.content.Context
import androidx.collection.ArraySet
import androidx.preference.PreferenceManager
import ar.edu.utn.frba.mobile.smarket.model.Contact
import com.google.gson.Gson

object ContactService {

    private const val PREFERENCE_KEY = "CONTACTS"
    private val gson = Gson()

    fun save(contact: Contact, context: Context) {
        var contacts = this.get(context)
        val actualContact = contacts.firstOrNull {it.name == contact.name}
        if (actualContact == null || actualContact.number != contact.number) {
            if (actualContact == null)
                contacts = contacts + contact
            else
                actualContact.number = contact.number

            val stringContacts = contacts.map { gson.toJson(it) }.toSet()
            val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
            preferenceManager.edit().putStringSet(PREFERENCE_KEY, stringContacts).apply()
        }
    }

    fun get(context: Context): List<Contact> {
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
        val stringContacts = preferenceManager.getStringSet(PREFERENCE_KEY, ArraySet())!!
        return stringContacts.map { gson.fromJson(it, Contact::class.java) }.sortedBy { it.name }
    }

}