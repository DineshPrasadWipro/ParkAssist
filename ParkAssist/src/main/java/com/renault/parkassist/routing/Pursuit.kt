package com.renault.parkassist.routing

import android.os.Parcel
import android.os.Parcelable

enum class Pursuit : Parcelable {
    MANEUVER,
    PARK
    ;

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(toInteger())
    }

    companion object CREATOR : Parcelable.Creator<Pursuit> {
        override fun createFromParcel(parcel: Parcel): Pursuit {
            return fromInteger(parcel.readInt())
        }

        override fun newArray(size: Int): Array<Pursuit?> {
            return arrayOfNulls(size)
        }

        private fun fromInteger(value: Int): Pursuit {
            return values()[value]
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    private fun toInteger(): Int {
        return ordinal
    }
}