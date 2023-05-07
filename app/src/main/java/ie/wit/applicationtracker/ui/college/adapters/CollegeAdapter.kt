package ie.wit.applicationtracker.ui.college.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.models.ApplicationModel

import ie.wit.applicationtracker.databinding.CollegeCardBinding

interface CollegeClickListener {
    fun onCollegeClick(application: ApplicationModel)
}

class CollegeAdapter constructor(private var applications: ArrayList<ApplicationModel>,
                                  private val listener: CollegeClickListener
)
    : RecyclerView.Adapter<CollegeAdapter.MainHolder>() {
    private var originalApplications: List<ApplicationModel> = applications.toList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CollegeCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val applications = applications[holder.adapterPosition]
        holder.bind(applications,listener)
    }



    fun removeAt(position: Int) {
        applications.removeAt(position)
        notifyItemRemoved(position)
    }
    fun filterApplication(query: String) {
        applications.clear()
        if (query.isEmpty()) {
            applications.addAll(originalApplications)
        } else {
            applications.addAll(originalApplications.filter { it.collegeName.contains(query, ignoreCase = true) })
        }
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = applications.size

    inner class MainHolder(val binding : CollegeCardBinding) :
                            RecyclerView.ViewHolder(binding.root) {

        fun bind(application: ApplicationModel, listener: CollegeClickListener) {
            binding.root.tag = application
            binding.application = application
            binding.collegeIcon.setImageResource(R.drawable.baseline_newspaper_24)
            binding.root.setOnClickListener { listener.onCollegeClick(application) }

            binding.executePendingBindings()
        }
    }
}