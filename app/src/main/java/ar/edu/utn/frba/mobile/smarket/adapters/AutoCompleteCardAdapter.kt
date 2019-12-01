package ar.edu.utn.frba.mobile.smarket.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Card
import ar.edu.utn.frba.mobile.smarket.model.CardType
import ar.edu.utn.frba.mobile.smarket.service.CardService
import com.google.android.material.button.MaterialButton

class AutoCompleteCardAdapter(context: Context, val cards: List<Card>, val onClick: (c: Card)->Unit) :
    ArrayAdapter<Card>(context, 0, cards), Filterable {

    private var cardItems: List<Card> = cards

    override fun getCount(): Int {
        return cardItems.count()
    }

    override fun getItem(p0: Int): Card? {
       return cardItems[p0]
    }

    override fun getItemId(p0: Int): Long {
        return cardItems[p0].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val newConvertView =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
        val textNumber = newConvertView.findViewById<TextView>(R.id.textCardNumberId)
        val imageCard = newConvertView.findViewById<ImageView>(R.id.imageCardType)
        val buttonRemove =  newConvertView.findViewById<MaterialButton>(R.id.buttonRemoveCard)
        val layoutCard = newConvertView.findViewById<ConstraintLayout>(R.id.layoutCardAutoComplete)
        val item = getItem(position)
        if (item != null) {
            textNumber.text = item.number
            buttonRemove.setOnClickListener {
                this.cardItems = this.cardItems.filter { it.number != item.number }
                item.enable = false
                val text = textNumber.text
                textNumber.text = ""
                textNumber.text = text
                CardService.remove(context, item)
            }
            layoutCard.setOnClickListener {
                onClick(item)
            }
            if (item.getType() == CardType.VISA)
                imageCard.setImageResource(R.mipmap.logo_visa)
            else
                imageCard.setImageResource(R.mipmap.logo_mastercard)
        }

        return newConvertView
    }

    override fun getFilter(): Filter {
        return object: Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()
                val filterResults = FilterResults()
                val suggestions = if (queryString == null || queryString.isEmpty())
                    cards
                else
                    cards.filter {
                        it.number.contains(queryString) && it.enable
                    }
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                @Suppress("UNCHECKED_CAST")
                cardItems = results.values as List<Card>
                notifyDataSetChanged()
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue!! as Card).number
            }
        }
    }

}