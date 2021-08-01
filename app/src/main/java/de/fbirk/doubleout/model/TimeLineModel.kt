package de.fbirk.doubleout.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class TimeLineModel (var title: String, var date: String, var description: String) : Parcelable