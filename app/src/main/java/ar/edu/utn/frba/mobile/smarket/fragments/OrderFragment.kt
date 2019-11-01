package ar.edu.utn.frba.mobile.smarket.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.enums.CardType
import ar.edu.utn.frba.mobile.smarket.enums.PurchaseStatus
import ar.edu.utn.frba.mobile.smarket.model.Product
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import ar.edu.utn.frba.mobile.smarket.service.PurchaseService
import kotlinx.android.synthetic.main.fragment_order.*
import java.util.*

class OrderFragment  : FragmentCommunication() {

    var totalPrice = 0.0
    lateinit var cardType: CardType

    override fun getFragment(): Int {
        return R.layout.fragment_order
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalPrice = activityCommunication.get("totalPrice") as Double
        textTotalPrice.text = totalPrice.toString()

        textSeeShoppingCart.setOnClickListener {
           val action = OrderFragmentDirections.actionOrderFragmentToShoppingCartFragment()
            findNavController().navigate(action)
        }

        @Suppress("UNCHECKED_CAST")
        buttonFinishOrder.setOnClickListener {
            val history = activityCommunication.get("history") as ArrayList<Purchase>
            val products = activityCommunication.get("products") as ArrayList<Product>
            val purchase = Purchase(UUID.randomUUID().toString(), Date(), totalPrice, products.size, products, PurchaseStatus.FINISHED
            )
            history.add(purchase)
            PurchaseService.savePurchase(purchase)
            findNavController().popBackStack()
        }

        textCardNumber.addTextChangedListener(object : TextWatcher {

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val text = textCardNumber.text.toString()
                val length = text.length

                imageCardLogo.setImageResource(
                    when(text[0]) {
                        '5' -> {
                            cardType = CardType.MASTERCARD
                            R.mipmap.ic_logo_mastercard
                        }
                        '4' -> {
                            cardType = CardType.VISA
                            R.mipmap.ic_logo_visa
                        }
                        '3' -> {
                            cardType = CardType.AMERICAN_EXPRESS
                            R.mipmap.ic_logo_american_express
                        }
                        else -> 0
                    })
                val textSplit = splitNumber(text.replace(" ", ""))
                if (textSplit != text) {
                    textCardNumber.setText(textSplit)
                    textCardNumber.setSelection(textSplit.length)
                }
                if (length == 19) {
                    textCardDueMonth.setSelection(0)
                    imageCardNumberStatus.setImageResource(R.mipmap.ic_success)
                    textCardSecurityCode.isEnabled = (length == 19)
                } else
                    imageCardNumberStatus.setImageResource(0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        textCardTitular.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                val split = textCardTitular.text.toString().split(" ")
                if (split.size > 1 && split[1].length > 1)
                    imageCardTitularStatus.setImageResource(R.mipmap.ic_success)
                else
                    imageCardTitularStatus.setImageResource(0)
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

                if (textCardDueYear.text.length == 2 && length > 0) {
                    val year = textCardDueYear.text.toString().toInt()
                    val month = text.toInt()
                    validateDate(year, month)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        textCardDueYear.filters = textCardDueYear.filters  + arrayOf(object : InputFilter {
            override fun filter(source: CharSequence,start: Int,end: Int,dest: Spanned,
                                dstart: Int,dend: Int): CharSequence? {
                val text = dest.toString() + source.toString()
                if (textCardDueMonth.text.length == 1) {
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
                imageCardDueDateStatus.setImageResource(0)
                if (lengthYear == 2) {
                    val year = textYear.toInt()
                    val month = textMonth.toInt()
                    validateDate(year, month)
                    textCardSecurityCode.setSelection(0)
                } else {
                    if (textCardDueMonth.text.length == 1)
                        textCardNumber.setText("0" + textCardDueMonth.text.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        textCardSecurityCode.filters = textCardSecurityCode.filters  + arrayOf(object : InputFilter {
            override fun filter(source: CharSequence,start: Int,end: Int,dest: Spanned,
                                dstart: Int,dend: Int): CharSequence? {
                val text = dest.toString()
                if (text.length == 3 && cardType != CardType.AMERICAN_EXPRESS)
                    return ""

                return null
            }
        })

        textCardSecurityCode.addTextChangedListener(object : TextWatcher {

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val limit =
                    if (cardType == CardType.AMERICAN_EXPRESS) 4
                    else 3

                when (textCardSecurityCode.text.toString().length) {
                    limit -> imageCardSecurityCodeStatus.setImageResource(R.mipmap.ic_success)
                    else -> imageCardSecurityCodeStatus.setImageResource(0)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        textContactName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                if (textContactName.text.isNotEmpty())
                    imageContactNameStatus.setImageResource(R.mipmap.ic_success)
                else
                    imageContactNameStatus.setImageResource(0)
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
                if (text.length == 9)
                    imageContactNumberStatus.setImageResource(R.mipmap.ic_success)
                else
                    imageContactNumberStatus.setImageResource(0)
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

    private fun validateDate(year: Int, month: Int) {
        if ((2000 + year) == Calendar.getInstance().get(Calendar.YEAR) &&
            month < Calendar.getInstance().get(Calendar.MONTH))
            imageCardDueDateStatus.setImageResource(R.mipmap.ic_error)
        else
            imageCardDueDateStatus.setImageResource(R.mipmap.ic_success)
    }
}
