package com.clumob.segment.controller

import android.os.Parcel
import android.os.Parcelable
import com.clumob.segment.controller.util.ParcelableUtil
import java.lang.reflect.InvocationTargetException

/**
 * Created by prashant.rathore on 02/02/18.
 */
open class SegmentInfo : Storable {

    var id = 0
    var arguments: Storable? = null
        private set
    var restorableSetmentState: Storable? = null
        private set

    internal constructor() {}
    constructor(id: Int, args: Storable?) {
        this.id = id
        arguments = args
    }

    protected constructor(input: Parcel) {
        id = input.readInt()
        try {
            arguments = readParcel<Storable>(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            restorableSetmentState = readParcel<Storable>(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(ClassNotFoundException::class, IllegalAccessException::class, InstantiationException::class, NoSuchMethodException::class, InvocationTargetException::class)
    private fun <T> readParcel(input: Parcel): T? {
        var parcelable: T? = null
        val readByteArrayLength = input.readInt()
        if (readByteArrayLength > 0) {
            val className = input.readString()
            val dataArray = ByteArray(readByteArrayLength)
            input.readByteArray(dataArray)
            val constructor = Class.forName(className).getDeclaredConstructor()
            constructor.isAccessible = true
            val creator: Parcelable.Creator<T> = constructor.newInstance() as Parcelable.Creator<T>
            parcelable = ParcelableUtil.unmarshall(dataArray, creator)
        }
        return parcelable
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(id)
        writeToParcel<Storable?>(arguments, parcel)
        writeToParcel<Storable?>(restorableSetmentState, parcel)
    }

    fun setRestorableSegmentState(restorableSetmentState: Storable?) {
        this.restorableSetmentState = restorableSetmentState
    }

    private fun <T : Storable?> writeToParcel(storable: T?, parcel: Parcel) {
        if (storable != null) {
            val creator = storable.creator()
                    ?: throw NullPointerException("Creator object cannot be null for " + javaClass.name)
            val aClass: Class<*> = creator.javaClass
            val aClassName = aClass.name
            val marshall = ParcelableUtil.marshall(storable)
            val length = marshall.size
            if (length > 0) {
                parcel.writeInt(length)
                parcel.writeString(aClassName)
                parcel.writeByteArray(marshall)
                return
            }
        }
        parcel.writeInt(-1)
    }

    override fun creator(): Parcelable.Creator<out SegmentInfo> {
        return CREATOR
    }

    companion object {
        private const val KEY_ID = "id"
        private const val KEY_PARAMS = "params"
        private const val KEY_SAVED_STATE = "savedState"
        @JvmField
        val CREATOR: Parcelable.Creator<SegmentInfo> = object : Parcelable.Creator<SegmentInfo> {
            override fun createFromParcel(input: Parcel): SegmentInfo {
                return SegmentInfo(input)
            }

            override fun newArray(size: Int): Array<SegmentInfo?> {
                return arrayOfNulls(size)
            }
        }
    }
}