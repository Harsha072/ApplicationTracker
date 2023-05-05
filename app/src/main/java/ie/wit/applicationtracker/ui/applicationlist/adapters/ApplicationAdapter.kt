package ie.wit.applicationtracker.ui.applicationlist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.models.ApplicationModel
import ie.wit.applicationtracker.databinding.ApplicationCardBinding

interface ApplicationClickListener {
    fun onApplicationClick(donation: ApplicationModel)
}

class ApplicationAdapter constructor(private var applications: ArrayList<ApplicationModel>,
                                  private val listener: ApplicationClickListener
)
    : RecyclerView.Adapter<ApplicationAdapter.MainHolder>() {
    private var originalApplications: List<ApplicationModel> = applications.toList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = ApplicationCardBinding
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

    inner class MainHolder(val binding : ApplicationCardBinding) :
                            RecyclerView.ViewHolder(binding.root) {

        fun bind(application: ApplicationModel, listener: ApplicationClickListener) {
            binding.root.tag = application
            binding.application = application
            binding.collegeIcon.setImageResource(R.drawable.baseline_newspaper_24)
            binding.root.setOnClickListener { listener.onApplicationClick(application) }
            binding.statusText.apply {
                text = application.status
                when (application.status) {
                    "Pending" -> setTextColor(ContextCompat.getColor(context, R.color.coloryellow))
                    "Rejected" -> setTextColor(ContextCompat.getColor(context, R.color.colorred))
                    else -> setTextColor(ContextCompat.getColor(context, R.color.colorgreen))
                }
            }
            binding.executePendingBindings()
        }
    }
}