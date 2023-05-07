package ie.wit.applicationtracker.ui.profile

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseUser
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.databinding.FragmentDetailsBinding
import ie.wit.applicationtracker.databinding.FragmentProfileBinding
import ie.wit.applicationtracker.firebase.FirebaseImageManager
import ie.wit.applicationtracker.models.ApplicationModel
import ie.wit.applicationtracker.models.UserModel
import ie.wit.applicationtracker.ui.addApplication.AddApplicationViewModel
import ie.wit.applicationtracker.ui.auth.LoggedInViewModel
import ie.wit.applicationtracker.ui.details.detailsFragmentArgs
import ie.wit.applicationtracker.utils.readImageUri
import ie.wit.applicationtracker.utils.showImagePicker
import timber.log.Timber
import java.util.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var intentLauncher : ActivityResultLauncher<Intent>
    private lateinit var profileViewModel: ProfileViewModel

    private var _fragBinding: FragmentProfileBinding? = null
    private val fragBinding get() = _fragBinding!!

    private lateinit var loggedInViewModel : LoggedInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.observableUser.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                render()
            }
        })
        val nameEditText = root.findViewById<EditText>(R.id.edit_text_name)
        val dobEditText = root.findViewById<EditText>(R.id.edit_text_dob)
        val addressEditText = root.findViewById<EditText>(R.id.edit_text_address)
        val phoneEditText = root.findViewById<EditText>(R.id.edit_text_phone)
        val citizenshipEditText = root.findViewById<EditText>(R.id.edit_text_citizenship)
        val gpaEditText = root.findViewById<EditText>(R.id.edit_gpa)
        val activitiesEditText = root.findViewById<EditText>(R.id.edit_activites)
        val saveButton = root.findViewById<Button>(R.id.saveButton)

        phoneEditText.inputType = InputType.TYPE_CLASS_NUMBER
        gpaEditText.inputType = InputType.TYPE_CLASS_NUMBER
        dobEditText.inputType = InputType.TYPE_NULL
        dobEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Set the selected date to the EditText
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                dobEditText.setText(selectedDate)
            }, year, month, dayOfMonth)
            datePickerDialog.show()
        }

        registerImagePickerCallback()
        fragBinding.profilePicture.setOnClickListener {
            showImagePicker(intentLauncher)
            Toast.makeText(requireContext(),"Click To Change Image", Toast.LENGTH_SHORT).show()
        }

        saveButton.setOnClickListener{
            // Get the values from the input fields
            val name = nameEditText.text.toString().trim()
            val dob = dobEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val citizenship = citizenshipEditText.text.toString().trim()
            val gpa = gpaEditText.text.toString().trim()
            val activities = activitiesEditText.text.toString().trim()

            // Check if all fields are non-empty
            if (listOf(name, dob, address, phone, citizenship, gpa, activities).any { it.isNullOrEmpty() }) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
            } else {
                // All values are non-empty, so proceed with saving the data
                // You can replace this with your own code to save the data
                Timber.i("getting the text::::   $name $dob $address $phone $citizenship $gpa $activities")
                Toast.makeText(context, "Data saved successfully", Toast.LENGTH_LONG).show()
                profileViewModel.addUser(loggedInViewModel.liveFirebaseUser,
                    UserModel(
                        name = name,
                        dob = dob,
                        address = address,
                        phone = phone,
                        citizenship = citizenship,
                        gpa = gpa,
                        activities = activities
                    )                )
                // Clear the input fields
                // Clear the input fields
                nameEditText.text = null
                dobEditText.text = null
                addressEditText.text = null
                phoneEditText.text = null
                citizenshipEditText.text = null
                gpaEditText.text = null
                activitiesEditText.text = null
            }
        }
        return root
    }

    private fun render() {
        Timber.i("render:::::")

        Timber.i("donation detail"+" "+profileViewModel.observableUser.value)

        fragBinding.profilevm = profileViewModel
         Timber.i("calling render:::::::::::::;;")
    }

    private fun updateNavHeader(currentUser: FirebaseUser) {
        Timber.i("nav hearder profile::::")
        FirebaseImageManager.imageUri.observe(viewLifecycleOwner) { result ->
            if (result == null) {
                Timber.i("inside::::: skipping null value")
                return@observe // Skip the initial null value
            }

            if (result == Uri.EMPTY) {
                Timber.i("DX NO Existing imageUri")
                if (currentUser.photoUrl != null) {
                    //if you're a google user
                    FirebaseImageManager.updateUserImage(
                        currentUser.uid,
                        currentUser.photoUrl,
                        fragBinding.profilePicture,
                        false
                    )
                } else {
                    Timber.i("DX Loading Existing Default imageUri")
                    FirebaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.img,
                        fragBinding.profilePicture
                    )
                }
            } else // load existing image from firebase
            {
                Timber.i("DX Loading Existing imageUri")
                FirebaseImageManager.updateUserImage(
                    currentUser.uid,
                    FirebaseImageManager.imageUri.value,
                    fragBinding.profilePicture, false
                )
            }
        }
        Timber.i("outside::::: ")
        fragBinding.emailAddress.text = currentUser.email
        if(currentUser.displayName != null)
            fragBinding.firstName.text = currentUser.displayName
    }

    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("DX registerPickerCallback() ${readImageUri(result.resultCode, result.data).toString()}")
                            FirebaseImageManager
                                .updateUserImage(loggedInViewModel.liveFirebaseUser.value!!.uid,
                                    readImageUri(result.resultCode, result.data),
                                    fragBinding.profilePicture,
                                    true)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
    override fun onResume() {
        Timber.i("on resume::::: ")
        super.onResume()

        profileViewModel.getUser(loggedInViewModel.liveFirebaseUser.value?.uid!!)

    }

    override fun onStart() {
        Timber.i("calling son start ")
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null) {
                updateNavHeader(firebaseUser)

            }
        })
    }
}
