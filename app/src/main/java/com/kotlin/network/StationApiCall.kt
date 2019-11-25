package com.kotlin.network

import com.kotlin.interfaces.GetDataListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class StationApiCall(url: String, listener: GetDataListener) {

    private val mListener = listener
    private val mUrl = url

    suspend fun connect() {
        makeCall(client)
    }

    private suspend fun makeCall(client: OkHttpClient) {
        withContext(Dispatchers.IO) {
            val request = Request.Builder().url(mUrl).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            mListener.onApiReturn(response.body?.string())
        }
    }

    companion object {
        val client = OkHttpClient()
    }
}
