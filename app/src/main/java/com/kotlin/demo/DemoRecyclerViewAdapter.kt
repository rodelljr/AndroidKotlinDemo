package com.kotlin.demo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.models.ResponseModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DemoRecyclerViewAdapter(context: Context, items: ArrayList<ResponseModel>) :
    RecyclerView.Adapter<DemoRecyclerViewAdapter.ViewHolder>() {

    private val mContext: Context
    private val mValues: ArrayList<ResponseModel>

    init {
        this.mContext = context
        this.mValues = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.main_frag_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mDuration.text = mValues.get(position).duration.toString()
        holder.mTime.text = getDateTime(mValues.get(position).risetime)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    private fun getDateTime(value: Long) : String {

        val date = Date(value * 1000)
        val sdf = SimpleDateFormat("MMM d yyyy HH:mm a")
        return sdf.format(date)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mDuration: TextView
        val mTime: TextView

        init {
            this.mDuration = itemView.findViewById(R.id.tv_duration_value)
            this.mTime = itemView.findViewById(R.id.tv_risetime_value)
        }
    }
}