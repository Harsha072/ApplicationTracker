package ie.wit.applicationtracker.ui.details

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.databinding.FragmentDetailsBinding
import ie.wit.applicationtracker.models.ApplicationModel
import ie.wit.applicationtracker.ui.applicationlist.ApplicationListViewModel
import ie.wit.applicationtracker.ui.auth.LoggedInViewModel
import timber.log.Timber
import java.util.*

class detailsFragment : Fragment() {

    private lateinit var detailViewModel: DetailsViewModel
    private val args by navArgs<detailsFragmentArgs>()
    private var _fragBinding: FragmentDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val applicationListViewModel : ApplicationListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        detailViewModel.observableApplication.observe(viewLifecycleOwner, Observer { render() })

        val collegeNameEditText = root.findViewById<TextInputEditText>(R.id.collegeNameEditText)
        val appliedDateEditText = root.findViewById<TextInputEditText>(R.id.appliedDateEditText)
        val applicationEndDateEditText = root.findViewById<TextInputEditText>(R.id.applicationEndDateEditText)
        val statusRadioGroup = root.findViewById<RadioGroup>(R.id.statusRadioGroup)
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

        fragBinding.editButton.setOnClickListener {
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
                Timber.i("getting the text updated ::::   $collegeName $appliedDate $applicationEndDate $selectedStatus")
                detailViewModel.updateApplication(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                    args.applicationid,  ApplicationModel(collegeName = collegeName, appliedDate = appliedDate, applicationEndDate = applicationEndDate, status = selectedStatus?:"", email = loggedInViewModel.liveFirebaseUser.value?.email!! )
                )
                // Clear the input fields
                collegeNameEditText.text = null
                appliedDateEditText.text = null
                applicationEndDateEditText.text = null
                statusRadioGroup.clearCheck()
            }

            findNavController().navigateUp()
        }
        fragBinding.deleteButton.setOnClickListener {
            applicationListViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observableApplication.value?.uid!!)
            findNavController().navigateUp()
        }


        return root
    }

    private fun render() {
        Timber.i("render:::::")

        Timber.i("donation detail"+" "+detailViewModel.observableApplication.value)

        fragBinding.applicationvm = detailViewModel
        when (detailViewModel.observableApplication.value?.status) {
            "Pending" -> fragBinding.statusPending.isChecked = true
            "Accepted" -> fragBinding.statusAccepted.isChecked = true
            "Rejected" -> fragBinding.statusRejected.isChecked = true
        }
        Timber.i("Retrofit fragBinding.donationvm == {${fragBinding.applicationvm}}")
    }

    override fun onResume() {
        Timber.i("on resume::::: ")
        super.onResume()

        detailViewModel.getApplication(loggedInViewModel.liveFirebaseUser.value?.uid!!, args.applicationid)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}