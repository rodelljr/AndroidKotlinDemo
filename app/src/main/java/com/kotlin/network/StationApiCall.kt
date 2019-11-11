package com.kotlin.network

import com.kotlin.interfaces.GetDataListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.CoroutineContext


class StationApiCall (url: String, listener: GetDataListener) : CoroutineScope {

     private val mListener = listener
     private val mUrl = url
     lateinit var job: Job

     fun connect() {
        job = Job()
        val client = OkHttpClient()
        makeCall(client)
    }

    private fun makeCall(client: OkHttpClient) = launch {
            val request = Request.Builder().url(mUrl).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                mListener.onApiReturn(response.body!!.string())
            }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

}