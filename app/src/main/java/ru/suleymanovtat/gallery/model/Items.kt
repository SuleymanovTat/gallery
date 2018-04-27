package ru.suleymanovtat.gallery.model

import android.os.Parcel
import android.os.Parcelable

data class Items(var file: String, var preview: String, var name: String, var public_url: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(file)
        writeString(preview)
        writeString(name)
        writeString(public_url)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Items> = object : Parcelable.Creator<Items> {
            override fun createFromParcel(source: Parcel): Items = Items(source)
            override fun newArray(size: Int): Array<Items?> = arrayOfNulls(size)
        }
    }
}
