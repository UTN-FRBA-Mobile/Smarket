package ar.edu.utn.frba.mobile.smarket.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Purchase(var id: Int,var date: Date, var price: Double, var amount: Int) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readInt(),Date(),parcel.readDouble(), parcel.readInt())

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