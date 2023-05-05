package ie.wit.applicationtracker.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.databinding.FragmentDetailsBinding
import ie.wit.applicationtracker.ui.applicationlist.ApplicationListViewModel
import ie.wit.applicationtracker.ui.auth.LoggedInViewModel
import timber.log.Timber

class detailsFragment : Fragment() {

    private lateinit var detailViewModel: DetailsViewModel
    private val args by navArgs<detailsFragmentArgs>()
    private var _fragBinding: FragmentDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ApplicationListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        detailViewModel.observableApplication.observe(viewLifecycleOwner, Observer { render() })




        fragBinding.editButton.setOnClickListener {
            detailViewModel.updateDonation(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.applicationid, fragBinding.applicationvm?.observableApplication!!.value!!)
            findNavController().navigateUp()
        }
        fragBinding.deleteButton.setOnClickListener {
            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observableApplication.value?.uid!!)
            findNavController().navigateUp()
        }


        return root
    }

    private fun render() {
        Timber.i("render:::::")

        Timber.i("donation detail"+" "+detailViewModel)
        fragBinding.applicationvm = detailViewModel
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