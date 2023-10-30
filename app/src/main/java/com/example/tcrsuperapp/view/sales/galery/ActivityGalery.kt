package com.example.tcrsuperapp.view.sales.galery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.adapter.AdapterGalery
import com.example.tcrsuperapp.api.ApiSales
import com.example.tcrsuperapp.model.Galery
import com.example.tcrsuperapp.view.sales.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.sales_activity_galery.*
import org.json.JSONObject

class ActivityGalery : AppCompatActivity() {
    lateinit var adapter: AdapterGalery
    lateinit var dataArrayList: ArrayList<Galery>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_galery)

        loadGalery()

        adapter = AdapterGalery(dataArrayList)
        val gridLayoutManager = GridLayoutManager(this, 2)
        recyclerGalery.setHasFixedSize(false)
        recyclerGalery.layoutManager = gridLayoutManager
        recyclerGalery.adapter = adapter

        backGalery.setOnClickListener {
            startActivity(Intent(this@ActivityGalery, ActivityBeranda::class.java))
            finish()
        }
    }

    private fun loadGalery() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiSales.GALERY)
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Galery(
                            dataArray.getJSONObject(i).getString("kode"),
                            dataArray.getJSONObject(i).getString("title"),
                            dataArray.getJSONObject(i).getString("img")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityGalery, ActivityBeranda::class.java))
        finish()
    }
}