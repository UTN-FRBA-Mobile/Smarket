package ar.edu.utn.frba.mobile.smarket.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Purchase() : Parcelable {

    var id = 0
    lateinit var date: Date
    var price: Double = 0.0
    var amount: Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        price = parcel.readDouble()
        amount = parcel.readInt()
    }

    constructor(id: Int, date: Date, price: Double, amount: Int) : this() {
        this.id = id
        this.date = date
        this.price = price
        this.amount = amount
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeDouble(price)
        parcel.writeInt(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Purchase> {
        override fun createFromParcel(parcel: Parcel): Purchase {
            return Purchase(parcel)
        }

        override fun newArray(size: Int): Array<Purchase?> {
            return arrayOfNulls(size)
        }
    }
}