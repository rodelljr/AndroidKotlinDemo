package com.kotlin.demo

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotlin.interfaces.GetDataListener
import com.kotlin.models.LocationModel
import com.kotlin.models.ResponseModel
import com.kotlin.network.StationApiCall
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.lang.reflect.Type


class MainActivity : AppCompatActivity(), GetDataListener, View.OnClickListener  {


    private var mLat = 0.0
    private var mLon = 0.0
    private var myList = emptyList<LocationModel>()
    private var myResponseList = arrayListOf<ResponseModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupSpinner()
        val buttonClick: Button = findViewById(R.id.btn_locate)
        buttonClick.setOnClickListener(this)
    }

    private fun setupSpinner() {
        val mSpinner: AppCompatSpinner = findViewById(R.id.sp_drop)
        val myAdapter =  ArrayAdapter(this, android.R.layout.simple_spinner_item, sportsLocations())
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinner.adapter = myAdapter
        mSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
            LocationModel("Sprint Center", 39.0974708, -94.5823416))

        return myList
    }

    private fun callFragment(models: ArrayList<ResponseModel>) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val frag = MainFragment.newInstance(models)
        transaction.replace(R.id.fl_layout, frag).commit()
    }

    private fun getAPIUrl(lat: Double, lon: Double): String {
        var builder = StringBuilder()
        builder.append("http://api.open-notify.org/iss-pass.json?lat=")
            .append(lat).append("&lon=").append(lon)
        return builder.toString()
    }

    override fun onClick(vCl: View?) {
        if (vCl != null) {
            if(vCl.id == R.id.btn_locate)
            {
                val myUrl = getAPIUrl(mLat, mLon)
                val client = StationApiCall(myUrl, this)
                client.connect()
            }
        }
    }

    override fun onApiReturn(result: String) {
        // call fragment and pass in model
        val theResult = getStationModel(result)
        callFragment(theResult)
    }

    private fun getStationModel(model: String): ArrayList<ResponseModel> {
        val gson = Gson()
        try {
            val obj = JSONObject(model)
            val res: JSONArray = obj.getJSONArray("response")
            val inputStream: InputStream = ByteArrayInputStream(res.toString().toByteArray())
            val reader: Reader = InputStreamReader(inputStream)
            val listType = object : TypeToken<ArrayList<ResponseModel>>(){}.type
            myResponseList = gson.fromJson(reader, listType)
            return myResponseList
        } catch(ue: UnsupportedEncodingException) {
            Log.e("Gson Encode Ex",ue.message)
            return myResponseList
        } catch (ex: Exception) {
            Log.e("Gson Ex",ex.message)
            return myResponseList
        }
    }

}
