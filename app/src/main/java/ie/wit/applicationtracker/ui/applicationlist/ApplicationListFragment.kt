package ie.wit.applicationtracker.ui.applicationlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.databinding.FragmentApplicationListBinding
import ie.wit.applicationtracker.main.ApplicationTracker
import ie.wit.applicationtracker.models.ApplicationModel
import ie.wit.applicationtracker.ui.applicationlist.adapters.ApplicationAdapter
import ie.wit.applicationtracker.ui.applicationlist.adapters.ApplicationClickListener
import ie.wit.applicationtracker.ui.auth.LoggedInViewModel
import ie.wit.applicationtracker.utils.*
import timber.log.Timber




class ApplicationListFragment : Fragment(), ApplicationClickListener {
    // TODO: Rename and change types of parameters
    lateinit var app: ApplicationTracker
    private var _fragListBinding: FragmentApplicationListBinding? = null
    private val fragListBinding get() = _fragListBinding!!
    private val applicationListViewModel: ApplicationListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    lateinit var loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        Timber.i("report ::::: ")
        _fragListBinding = FragmentApplicationListBinding.inflate(inflater, container, false)
        val root = fragListBinding.root
        setupMenu()
        loader = createLoader(requireActivity())

        fragListBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
//        reportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
        showLoader(loader,"Downloading")
        applicationListViewModel.observableDonationsList.observe(viewLifecycleOwner, Observer {
                applications ->
            applications?.let {
                render(applications as ArrayList<ApplicationModel>)
                hideLoader(loader)
                checkSwipeRefresh()
            }
        })

        fragListBinding.fab.setOnClickListener {

            findNavController().navigate(ApplicationListFragmentDirections.actionApplicationListFragmentToAddApplicationFragment())
        }

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Donation")
                val adapter = fragListBinding.recyclerView.adapter as ApplicationAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                applicationListViewModel.delete(applicationListViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as ApplicationModel).uid.toString())


                hideLoader(loader)
            }
        }
        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onApplicationClick(viewHolder.itemView.tag as ApplicationModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragListBinding.recyclerView)

        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragListBinding.recyclerView)

        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//
                menuInflater.inflate(R.menu.menu_list, menu)
                val searchItem = menu.findItem(R.id.search)
                val searchView = searchItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let {
                            val adapter = fragListBinding.recyclerView.adapter as ApplicationAdapter
                            adapter.filterApplication(it)
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
//
    private fun render(applicationList: ArrayList<ApplicationModel>) {
        fragListBinding.recyclerView.adapter = ApplicationAdapter(applicationList,this)
        if (applicationList.isEmpty()) {
            fragListBinding.recyclerView.visibility = View.GONE
            fragListBinding.applicationNotFound.visibility = View.VISIBLE
        } else {
            fragListBinding.recyclerView.visibility = View.VISIBLE
            fragListBinding.applicationNotFound.visibility = View.GONE
        }
    }

    override fun onApplicationClick(applications: ApplicationModel) {
//        val action = ApplicationListFragmentDirections.actionApplicationListFragmentToApplicationDetailFragment(applications.uid.toString())
        findNavController().navigate(ApplicationListFragmentDirections.actionApplicationListFragmentToApplicationDetailFragment(applications.uid.toString())
        )
    }

    fun setSwipeRefresh() {
        fragListBinding.swiperefresh.setOnRefreshListener {
            fragListBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Donations")
            applicationListViewModel.load()
        }
    }

    fun checkSwipeRefresh() {
        if (fragListBinding.swiperefresh.isRefreshing)
            fragListBinding.swiperefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader,"Downloading Donations")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                applicationListViewModel.liveFirebaseUser.value = firebaseUser
                applicationListViewModel.load()
            }
        })
        //hideLoader(loader)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        fragListBinding = null
    }


}