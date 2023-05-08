package ie.wit.applicationtracker.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class ItemList(

    val itemList: MutableList<String>): Parcelable {
    constructor() : this(mutableListOf())
    @Exclude
    fun toMap(): Map<String, Any?> {

        return mapOf(

            "itemList" to itemList
        )
    }
}