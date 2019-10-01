package ar.edu.utn.frba.mobile.smarket.model

import android.os.Parcel
import android.os.Parcelable

class Product(var id: Int, var amount: Int, var description: String?, var price: Double) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readDouble()
    )


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if (dest != null) {
            dest.writeInt(id)
            dest.writeString(description)
            dest.writeDouble(price)
            dest.writeInt(amount)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}