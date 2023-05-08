package ie.wit.applicationtracker.ui.checklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.databinding.FragmentChecklistBinding
import ie.wit.applicationtracker.ui.auth.LoggedInViewModel
import ie.wit.applicationtracker.ui.checklist.adapters.ItemAdapter
import timber.log.Timber

class ChecklistFragment : Fragment() {

    companion object {
        fun newInstance() = ChecklistFragment()
    }
    private var _fragListBinding: FragmentChecklistBinding? = null
    private val fragListBinding get() = _fragListBinding!!
    private val viewModel: ChecklistViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private val args by navArgs<ChecklistFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragListBinding = FragmentChecklistBinding.inflate(inflater, container, false)
        val root = fragListBinding.root
        itemAdapter =  ItemAdapter(viewModel.observableList.value?.itemList, this)
        recyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = itemAdapter

        viewModel.observableList.observe(viewLifecycleOwner, Observer {    list ->
            list?.let {
                Timber.i("got list "+list)
                itemAdapter.getItems().itemList.clear()
                itemAdapter.getItems().itemList.addAll(list.itemList)
                itemAdapter.notifyDataSetChanged()


            } })


        val addButton: Button = root.findViewById(R.id.add_button)
        val saveButton : Button = root.findViewById(R.id.save_button)
        val editText: EditText = root.findViewById(R.id.item_edit_text)
        addButton.setOnClickListener {
            val newItem = editText.text.toString()
            itemAdapter.addItem(newItem)
            editText.text=null
        }
        saveButton.setOnClickListener {
            viewModel.saveChecklist(loggedInViewModel.liveFirebaseUser,itemAdapter.getItems(),args.applicationid)
        }

        return root
    }

    private fun render() {
        Timber.i("on render::::: ")
        val adapter = fragListBinding.recyclerView.adapter as ItemAdapter
        adapter.getItems().itemList.clear()
        val itemList = fragListBinding.listvm?.observableList?.value?.itemList ?: emptyList()
        Timber.i("item list "+itemList)
        adapter.getItems().itemList.addAll(itemList)
        adapter.notifyDataSetChanged()
    }
    override fun onResume() {
        Timber.i("on resume::::: ")
        super.onResume()

        viewModel.getCheckList(args.applicationid,)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}