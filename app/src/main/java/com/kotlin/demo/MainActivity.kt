package com.kotlin.demo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotlin.interfaces.GetDataListener
import com.kotlin.models.LocationModel
import com.kotlin.models.Response
import com.kotlin.models.ResponseModel
import com.kotlin.network.StationApiCall
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), GetDataListener, View.OnClickListener, CoroutineScope {

    private var mLat = 0.0
    private var mLon = 0.0
    private var myList = emptyList<LocationModel>()
    private var myResponseList = arrayListOf<ResponseModel>()
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupSpinner()
        val buttonClick: Button = findViewById(R.id.btn_locate)
        buttonClick.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun setupSpinner() {
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sportsLocations())
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_drop.adapter = myAdapter
        sp_drop.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mLat = myList[position].latitude
                mLon = myList[position].longitude
            }
        }
    }

    private fun sportsLocations(): List<LocationModel> {
        myList = listOf(
            LocationModel("Arrowhead Stadium", 39.0527456, -94.490726),
            LocationModel("Kauffman Stadium", 39.0489432, -94.4861044),
            LocationModel("Children\'s Mercy Park", 39.121597, -94.8253654),
            LocationModel("Sprint Center", 39.0974708, -94.5823416)
        )

        return myList
    }

    private fun callFragment(models: ArrayList<ResponseModel>) {
        supportFragmentManager.beginTransaction().apply {
            val frag = MainFragment.newInstance(models)
            this.replace(R.id.fl_layout, frag).commit()
        }
    }

    private fun getAPIUrl(lat: Double, lon: Double): String {
        val builder = StringBuilder()
        builder.append("http://api.open-notify.org/iss-pass.json?lat=")
            .append(lat).append("&lon=").append(lon)
        return builder.toString()
    }

    override fun onClick(vCl: View?) {
        if (vCl == btn_locate) {
            val myUrl = getAPIUrl(mLat, mLon)
            val client = StationApiCall(myUrl, this)
            launch {
                client.connect()
            }
        }
    }

    override fun onApiReturn(result: String?) {
        // call fragment and pass in model
        val theResult = getStationModel(result)
        callFragment(theResult)
    }

    private fun getStationModel(model: String?): ArrayList<ResponseModel> {
        if (model == null)
            return myResponseList
        val gson = Gson()
        try {
            val responseData = gson.fromJson(model, Response::class.java)
            myResponseList = responseData.response
        } catch (ue: UnsupportedEncodingException) {
            Log.e("Gson Encode Ex", ue.message)
        } catch (ex: Exception) {
            Log.e("Gson Ex", ex.message)
        }
        return myResponseList
    }
}
