package com.renault.parkassist.routing

import android.os.Parcel
import android.os.Parcelable

enum class RouteIdentifier : Parcelable {
    SURROUND_AVM_MAIN,
    SURROUND_AVM_TRAILER,
    SURROUND_AVM_SETTINGS,
    SURROUND_AVM_DEALER,
    SURROUND_MVC_MAIN,
    SURROUND_MVC_TRAILER,
    SURROUND_MVC_SETTINGS,
    SURROUND_MVC_DEALER,
    SURROUND_RVC_MAIN,
    SURROUND_RVC_TRAILER,
    SURROUND_RVC_SETTINGS,
    SURROUND_RVC_DEALER,
    SURROUND_AVM_POPUP,
    SURROUND_WARNING,
    SONAR_POPUP,
    PARKING_AVM_HFP_SCANNING,
    PARKING_AVM_HFP_GUIDANCE,
    PARKING_AVM_HFP_PARK_OUT,
    PARKING_MVC_HFP_SCANNING,
    PARKING_MVC_HFP_GUIDANCE,
    PARKING_MVC_HFP_PARK_OUT,
    PARKING_RVC_HFP_SCANNING,
    PARKING_RVC_HFP_GUIDANCE,
    PARKING_RVC_HFP_PARK_OUT,
    PARKING_FAPK_SCANNING,
    PARKING_FAPK_GUIDANCE,
    PARKING_FAPK_PARK_OUT,
    PARKING_WARNING,
    NONE,
    ;

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(toInteger())
    }

    companion object CREATOR : Parcelable.Creator<RouteIdentifier> {
        override fun createFromParcel(parcel: Parcel): RouteIdentifier {
            return fromInteger(parcel.readInt())
        }

        override fun newArray(size: Int): Array<RouteIdentifier?> {
            return arrayOfNulls(size)
        }

        private fun fromInteger(value: Int): RouteIdentifier {
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