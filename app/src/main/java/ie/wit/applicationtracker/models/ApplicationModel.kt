package ie.wit.applicationtracker.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize




@IgnoreExtraProperties
@Parcelize
data class ApplicationModel(
      var uid: String? = "",
      var collegeName: String = "N/A",
      var appliedDate: String = "",
      var applicationEndDate: String = "",
      var status: String = "",
      var email: String? = "joe@bloggs.com")
      : Parcelable
{
      @Exclude
      fun toMap(): Map<String, Any?> {
            return mapOf(
                  "uid" to uid,
                  "collegeName" to collegeName,
                  "appliedDate" to appliedDate,
                  "applicationEndDate" to applicationEndDate,
                  "status" to status,
            "email" to email
            )
      }
}



