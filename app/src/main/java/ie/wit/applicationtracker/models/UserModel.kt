package ie.wit.applicationtracker.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class UserModel(
    var uid: String? = "",
    var name: String? = "",
    var dob: String? = "",
    var address: String? = "",
    var phone: String? = null,
    var citizenship: String? = "",
    var gpa: String? = null,
    var activities: String? = ""
) : Parcelable {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "name" to name,
            "dob" to dob,
            "address" to address,
            "phone" to phone,
            "citizenship" to citizenship,
            "gpa" to gpa,
            "activities" to activities
        )
    }
}
