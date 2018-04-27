package ru.suleymanovtat.gallery.model

import android.os.Parcel
import android.os.Parcelable

data class ImageData(var items: ArrayList<Items>) : Parcelable {
    constructor(source: Parcel) : this(
            source.createTypedArrayList(Items.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(items)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ImageData> = object : Parcelable.Creator<ImageData> {
            override fun createFromParcel(source: Parcel): ImageData = ImageData(source)
            override fun newArray(size: Int): Array<ImageData?> = arrayOfNulls(size)
        }
    }
}


