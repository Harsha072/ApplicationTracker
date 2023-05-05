package ie.wit.applicationtracker.ui.addApplication

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.databinding.FragmentAddApplicationBinding
import ie.wit.applicationtracker.models.ApplicationModel
import ie.wit.applicationtracker.ui.auth.LoggedInViewModel
import ie.wit.applicationtracker.utils.toDate
import ie.wit.applicationtracker.utils.toDateString
import timber.log.Timber
import java.util.*

class addApplicationFragment : Fragment() {

    private var _addAppBinding: FragmentAddApplicationBinding? = null
    private val fragBinding get() = _addAppBinding!!
    private lateinit var addAppViewModel: AddApplicationViewModel
//    private val reportViewModel: ReportViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            // Inflate the layout for this fragment
//            val view = inflater.inflate(R.layout.fragment_add_application, container, false)
            _addAppBinding = FragmentAddApplicationBinding.inflate(inflater, container, false)
            val root = fragBinding.root
            addAppViewModel = ViewModelProvider(this).get(AddApplicationViewModel::class.java)
            // Find views by ID
            val collegeNameEditText = root.findViewById<TextInputEditText>(R.id.collegeNameEditText)
            val appliedDateEditText = root.findViewById<TextInputEditText>(R.id.appliedDateEditText)
            val applicationEndDateEditText = root.findViewById<TextInputEditText>(R.id.applicationEndDateEditText)
            val statusRadioGroup = root.findViewById<RadioGroup>(R.id.statusRadioGroup)
            val saveButton = root.findViewById<Button>(R.id.saveButton)
            appliedDateEditText.clearFocus()
            appliedDateEditText.setOnClickListener {
                // on below line we are getting
                // the instance of our calendar.
                val c = Calendar.getInstance()

                // on below line we are getting
                // our day, month and year.
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                // on below line we are creating a
                // variable for date picker dialog.
                val datePickerDialog = DatePickerDialog(
                    // on below line we are passing context.
                    requireContext(),
                    { root, year, monthOfYear, dayOfMonth ->
                        // on below line we are setting
                        // date to our edit text.
                        val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        appliedDateEditText.setText(dat)
                    },
                    // on below line we are passing year, month
                    // and day for the selected date in our date picker.
                    year,
                    month,
                    day
                )
                // at last we are calling show
                // to display our date picker dialog.
                datePickerDialog.show()

            }
            applicationEndDateEditText.setOnClickListener {
                // on below line we are getting
                // the instance of our calendar.
                val c = Calendar.getInstance()

                // on below line we are getting
                // our day, month and year.
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                // on below line we are creating a
                // variable for date picker dialog.
                val datePickerDialog = DatePickerDialog(
                    // on below line we are passing context.
                    requireContext(),
                    { root, year, monthOfYear, dayOfMonth ->
                        // on below line we are setting
                        // date to our edit text.
                        val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        applicationEndDateEditText.setText(dat)
                    },
                    // on below line we are passing year, month
                    // and day for the selected date in our date picker.
                    year,
                    month,
                    day
                )
                // at last we are calling show
                // to display our date picker dialog.
                datePickerDialog.show()

            }
            // Set up click listener for the Save button
            saveButton.setOnClickListener {
                // Get the values from the input fields
                val collegeName = collegeNameEditText.text.toString()
                val appliedDate = appliedDateEditText.text.toString()
                val applicationEndDate = applicationEndDateEditText.text.toString()
                val selectedStatus = when (statusRadioGroup.checkedRadioButtonId) {
                    R.id.statusPending -> "Pending"
                    R.id.statusAccepted -> "Accepted"
                    R.id.statusRejected -> "Rejected"
                    else -> null
                }

                if (listOf(collegeName, appliedDate, applicationEndDate, selectedStatus).any { it.isNullOrEmpty() }) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
                } else {
                    // All values are non-empty, so proceed with saving the application data
                    Timber.i("getting the text::::   $collegeName $appliedDate $applicationEndDate $selectedStatus")
                    addAppViewModel.addApplication(loggedInViewModel.liveFirebaseUser,
                        ApplicationModel(collegeName = collegeName, appliedDate = appliedDate, applicationEndDate = applicationEndDate, status = selectedStatus?:"", email = loggedInViewModel.liveFirebaseUser.value?.email!! ))
                    // Clear the input fields
                    collegeNameEditText.text = null
                    appliedDateEditText.text = null
                    applicationEndDateEditText.text = null
                    statusRadioGroup.clearCheck()
                }
                val navController = findNavController()
                navController.popBackStack()


            }

            return root
        }
    override fun onDestroyView() {
        super.onDestroyView()
        _addAppBinding = null
    }
        override fun onResume() {
        super.onResume()

    }
    //    private fun setupMenu() {
//        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
//            override fun onPrepareMenu(menu: Menu) {
//                // Handle for example visibility of menu items
//            }
//
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.menu_donate, menu)
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                // Validate and handle the selected menu item
//                return NavigationUI.onNavDestinationSelected(menuItem,
//                    requireView().findNavController())
//            }
//        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
//    }
    }











//}