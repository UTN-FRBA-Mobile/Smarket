package ar.edu.utn.frba.mobile.smarket.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Card
import ar.edu.utn.frba.mobile.smarket.model.Contact
import ar.edu.utn.frba.mobile.smarket.service.ContactService
import com.google.android.material.button.MaterialButton

class AutoCompleteContactAdapter(context: Context, val contacts: List<Contact>, val onClick: (c: Contact)->Unit) :
    ArrayAdapter<Contact>(context, 0, contacts), Filterable {

    private var contactItems: List<Contact> = contacts

    override fun getCount(): Int {
        return contactItems.count()
    }

    override fun getItem(p0: Int): Contact? {
        return contactItems[p0]
    }

    override fun getItemId(p0: Int): Long {
        return contactItems[p0].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val newConvertView =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
        val textName = newConvertView.findViewById<TextView>(R.id.textContactNameId)
        val buttonRemove =  newConvertView.findViewById<MaterialButton>(R.id.buttonRemoveContact)
        val layoutContact = newConvertView.findViewById<ConstraintLayout>(R.id.layoutContactAutoComplete)
        val item = getItem(position)
        if (item != null) {
            textName.text = item.name
            buttonRemove.setOnClickListener {
                this.contactItems = this.contactItems.filter { it.number != item.number }
                item.enable = false
                val text = textName.text
                textName.text = ""
                textName.text = text
                ContactService.remove(context, item)
            }
            layoutContact.setOnClickListener {
                onClick(item)
            }
        }

        return newConvertView
    }

    override fun getFilter(): Filter {
        return object: Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()
                val filterResults = FilterResults()
                if (contacts.any { it.name == queryString}) {
                    filterResults.values = ArrayList<Card>()
                    filterResults.count = 0
                    return filterResults
                }
                val suggestions = if (queryString == null || queryString.isEmpty())
                    contacts
                else
                    contacts.filter {
                        it.name.contains(queryString) && it.enable
                    }
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                @Suppress("UNCHECKED_CAST")
                contactItems = results.values as List<Contact>
                notifyDataSetChanged()
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue!! as Contact).name
            }
        }
    }

}