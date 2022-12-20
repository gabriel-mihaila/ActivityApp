package com.example.activityapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.activityapp.R
import com.example.activityapp.model.ActivityItem

class ActivityAdapter(private val activities: ArrayList<ActivityItem>) :
    RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val activityView: TextView = view.findViewById(R.id.activity_text)
        val accessibilityView: TextView = view.findViewById(R.id.accessibility_text)
        val typeView: TextView = view.findViewById(R.id.type_text)
        val participantsView: TextView = view.findViewById(R.id.participants_text)
        val priceView: TextView = view.findViewById(R.id.price_text)
        val linkView: TextView = view.findViewById(R.id.link_text)
        val keyView: TextView = view.findViewById(R.id.key_text)
        val starView: ImageView = view.findViewById(R.id.favourite)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.activity_item, viewGroup, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val activity = activities[i]
        viewHolder.activityView.text = activity.activity
        viewHolder.accessibilityView.text = activity.accessibility.toString()
        viewHolder.typeView.text = activity.type
        viewHolder.participantsView.text = activity.participants.toString()
        viewHolder.priceView.text = activity.price.toString()
        viewHolder.linkView.text = activity.link
        viewHolder.keyView.text = activity.key
        viewHolder.starView.visibility = if (activity.star) View.VISIBLE else View.INVISIBLE
        viewHolder.itemView.setBackgroundColor(if (activity.shared) Color.LTGRAY else Color.WHITE)
        viewHolder.itemView.setOnClickListener {
            activity.star = !activity.star
            viewHolder.starView.visibility = if (activity.star) View.VISIBLE else View.INVISIBLE
        }
        viewHolder.itemView.setOnLongClickListener {
            viewHolder.itemView.showContextMenu()
        }
    }

    override fun getItemCount() = activities.size
}
