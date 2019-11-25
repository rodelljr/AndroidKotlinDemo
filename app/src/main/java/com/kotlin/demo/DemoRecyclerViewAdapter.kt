package com.kotlin.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.models.ResponseModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.main_frag_item.*
import java.text.SimpleDateFormat
import java.util.*

class DemoRecyclerViewAdapter(private val items: ArrayList<ResponseModel>) :
    RecyclerView.Adapter<DemoRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_frag_item, parent, false)
        return DemoRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: DemoRecyclerViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount() = items.size
}

class DemoRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
    override val containerView: View? = itemView

    fun bindView(item: ResponseModel) {
        tv_duration_value.text = item.duration.toString()
        tv_risetime_value.text = getDateTime(item.risetime)
    }

    private fun getDateTime(value: Long): String {
        val date = Date(value * 1000)
        val sdf = SimpleDateFormat("MMM d yyyy HH:mm a", Locale.US)
        return sdf.format(date)
    }
}
