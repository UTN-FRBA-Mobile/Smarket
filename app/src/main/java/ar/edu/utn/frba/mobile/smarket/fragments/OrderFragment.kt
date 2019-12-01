package ar.edu.utn.frba.mobile.smarket.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.*
import android.view.View
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.adapters.AutoCompleteCardAdapter
import ar.edu.utn.frba.mobile.smarket.adapters.AutoCompleteContactAdapter
import ar.edu.utn.frba.mobile.smarket.enums.PurchaseStatus
import ar.edu.utn.frba.mobile.smarket.model.*
import ar.edu.utn.frba.mobile.smarket.service.CardService
import ar.edu.utn.frba.mobile.smarket.service.ContactService
import ar.edu.utn.frba.mobile.smarket.service.HistoryService
import kotlinx.android.synthetic.main.fragment_order.*
import java.util.*
import kotlin.collections.ArrayList

class OrderFragment  : FragmentCommunication() {

    var totalPrice = 0.0
    var contacts = ArrayList<Contact>().toList()
    var cards = ArrayList<Card>().toList()

    override fun getFragment(): Int {
        return R.layout.fragment_order
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalPrice = mainActivity.mViewModel.totalPrice
        textTotalPrice.text = totalPrice.toString()
        cards = CardService.get(this.context!!)

        val cardAdapter = AutoCompleteCardAdapter(context!!, cards) {
            autoCompleteCardNumber.setText(it.number)
            textCardDueMonth.setText(it.month)
            textCardDueYear.setText(it.year)
            textCardTitular.setText(it.titular)
        }
        autoCompleteCardNumber.setAdapter(cardAdapter)

        contacts = ContactService.get(this.context!!)

        val contactAdapter = AutoCompleteContactAdapter(context!!, contacts) {
            autoCompleteContactName.setText(it.name)
            textContactNumber.setText(it.number)
        }

        autoCompleteContactName.setAdapter(contactAdapter)

        textCardNumberController.isEndIconVisible = false
        textCardTitularController.isEndIconVisible = false
        textContactNumberController.isEndIconVisible = false
        textContactNameController.isEndIconVisible = false
        dateInputLayoutController.isEndIconVisible = false
        textCardSecurityCode.isEnabled = false
        setButtonFinishEnabled()

        textSeeShoppingCart.setOnClickListener {
           val action = OrderFragmentDirections.actionOrderFragmentToShoppingCartFragment()
            findNavController().navigate(action)
        }

        @Suppress("UNCHECKED_CAST")
        buttonFinishOrder.setOnClickListener {
            val history = mainActivity.mViewModel.history!!
            val products = mainActivity.mViewModel.purchases!!
            val purchase = History(UUID.randomUUID().toString(), Date(), totalPrice, products.size, products, PurchaseStatus.PENDING)
            val card = Card(autoCompleteCardNumber, textCardDueYear, textCardDueMonth, textCardTitular)
            val contact = Contact(autoCompleteContactName, textContactNumber)

            history.add(purchase)
            HistoryService.savePurchase(purchase)

            CardService.save(card, this.context!!)
            ContactService.save(contact, this.context!!)

            findNavController().popBackStack()
        }

        autoCompleteCardNumber.addTextChangedListener(object : TextWatcher {

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val text = autoCompleteCardNumber.text.toString()
                val length = text.length

                val textSplit = splitNumber(text.replace(" ", ""))
                if (textSplit != text) {
                    autoCompleteCardNumber.setText(textSplit)
                    autoCompleteCardNumber.setSelection(textSplit.length)
                }
                if (length == 19) {
                    textCardDueMonth.setSelection(0)
                    textCardSecurityCode.isEnabled = (length == 19)
                    textCardNumberController.isEndIconVisible = true
                    textCardSecurityCode.isEnabled = true
                } else{
                    textCardNumberController.isEndIconVisible = false
                    textCardSecurityCode.isEnabled = false
                    textCardSecurityCode.setText("")
                }
                setButtonFinishEnabled()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        textCardTitular.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                val split = textCardTitular.text.toString().split(" ")
                textCardTitularController.isEndIconVisible = (split.size > 1 && split[0].length > 1)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        textCardDueMonth.filters = textCardDueMonth.filters  + arrayOf(object : InputFilter {
            override fun filter(source: CharSequence,start: Int,end: Int,dest: Spanned,
                dstart: Int,dend: Int): CharSequence? {

                val text = dest.toString() + source.toString()
                if (text.length == 2) {
                    val month = Integer.parseInt(text)
                    if (month !in 1..12)
                        return ""
                }
                return null
            }
        })

        textCardDueMonth.addTextChangedListener(object : TextWatcher {

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val text = textCardDueMonth.text.toString()
                val length = text.length
                if (length == 2)
                    textCardDueYear.setSelection(0)

                if (textCardDueYear.text!!.length == 2 && length > 0) {
                    val year = textCardDueYear.text.toString().toInt()
                    val month = text.toInt()
                    dateInputLayoutController.isEndIconVisible = validateDate(year, month)
                }
                setButtonFinishEnabled()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        textCardDueYear.filters = textCardDueYear.filters  + arrayOf(object : InputFilter {
            override fun filter(source: CharSequence,start: Int,end: Int,dest: Spanned,
                                dstart: Int,dend: Int): CharSequence? {
                val text = dest.toString() + source.toString()
                if (textCardDueMonth.text!!.length == 1) {
                    val year = Integer.parseInt(dest.toString() + source.toString())
                    if (year == 0)
                        return ""
                } else if (text.length == 2) {
                    val year = Integer.parseInt(text)
                    if (year < 19)
                        return ""
                }
                return null
            }
        })

        textCardDueYear.addTextChangedListener(object : TextWatcher {

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val textYear = textCardDueYear.text.toString()
                val lengthYear = textYear.length
                val textMonth = textCardDueMonth.text.toString()
                if (lengthYear == 2) {
                    val year = textYear.toInt()
                    val month = textMonth.toInt()
                    dateInputLayoutController.isEndIconVisible = validateDate(year, month)
                } else
                    dateInputLayoutController.isEndIconVisible = false
                
                setButtonFinishEnabled()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        textCardSecurityCode.filters = textCardSecurityCode.filters  + arrayOf(object : InputFilter {
            override fun filter(source: CharSequence,start: Int,end: Int,dest: Spanned,
                                dstart: Int,dend: Int): CharSequence? {
                val text = dest.toString()
                if (text.length == CardService.logMaxSecurityCode(autoCompleteCardNumber.text.toString()))
                    return ""

                return null
            }
        })

        textCardSecurityCode.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val limit = CardService.logMaxSecurityCode(autoCompleteCardNumber.text.toString())

                when (textCardSecurityCode.text.toString().length) {
                    limit -> imageCardSecurityCodeStatus.setImageResource(R.mipmap.ic_success)
                    else -> imageCardSecurityCodeStatus.setImageResource(0)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        autoCompleteContactName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                textContactNameController.isEndIconVisible = (!TextUtils.isEmpty(autoCompleteContactName.text!!))
                setButtonFinishEnabled()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        textContactNumber.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                val text = textContactNumber.text.toString()
                val textSplit = splitNumber(text.replace(" ", ""))
                if (textSplit != text) {
                    textContactNumber.setText(textSplit)
                    textContactNumber.setSelection(textSplit.length)
                }

                textContactNumberController.isEndIconVisible = (text.length == 9)
                setButtonFinishEnabled()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }

    private fun splitNumber(text: String) : String {
        return if (text.length > 4) {
            val subString = text.substring(0, 4)
            subString + " " + splitNumber(text.substring(4))
        } else
            text
    }

    private fun validateDate(year: Int, month: Int):Boolean {
        return !((2000 + year) == Calendar.getInstance().get(Calendar.YEAR)
                        && month < Calendar.getInstance().get(Calendar.MONTH))
    }

    private fun setButtonFinishEnabled(){
        buttonFinishOrder.isEnabled = buttonFinishEnabled()
    }

    private fun buttonFinishEnabled(): Boolean{
        return textCardNumberController.isEndIconVisible
                && textCardTitularController.isEndIconVisible
                && textContactNumberController.isEndIconVisible
                && textContactNameController.isEndIconVisible
                && dateInputLayoutController.isEndIconVisible
    }
}
