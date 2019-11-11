package com.kotlin.demo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.models.ResponseModel

class MainFragment : Fragment() {

    private var myResponseList: ArrayList<ResponseModel> = ArrayList<ResponseModel>()

    companion object {
        const val MODEL_KEY: String = "model_key"

        fun newInstance(model: ArrayList<ResponseModel>):MainFragment {
            val args = Bundle()
            args.putParcelableArrayList(MODEL_KEY, model)
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myResponseList = arguments!!.getParcelableArrayList(MODEL_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_frag_item_list, container, false)
        if(view is RecyclerView) {
            val myContext: Context = view.context
            val recyclerView : RecyclerView = view
            recyclerView.layoutManager = LinearLayoutManager(myContext)
            recyclerView.adapter = DemoRecyclerViewAdapter(myContext, myResponseList)
        }

        return view
    }

}