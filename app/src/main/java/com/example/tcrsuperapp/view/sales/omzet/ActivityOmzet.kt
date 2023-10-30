package com.example.tcrsuperapp.view.sales.omzet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.adapter.AdapterOmzet
import com.example.tcrsuperapp.api.ApiSales
import com.example.tcrsuperapp.model.Omzet
import com.example.tcrsuperapp.view.sales.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.sales_activity_omzet.*
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat

class ActivityOmzet : AppCompatActivity() {
    lateinit var SP: SharedPreferences
    lateinit var adapter: AdapterOmzet
    lateinit var dataArrayList: ArrayList<Omzet>
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_omzet)

        SP = applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        loadOmzet()

        adapter = AdapterOmzet(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerOmzet.layoutManager = layoutManager
        recyclerOmzet.adapter = adapter

        backOmzet.setOnClickListener {
            startActivity(Intent(this@ActivityOmzet, ActivityBeranda::class.java))
            finish()
        }
    }

    private fun loadOmzet() {
        dataArrayList = ArrayList()
        val fetchData1 = FetchData(ApiSales.OMZET)
        if (fetchData1.startFetch()) {
            if (fetchData1.onComplete()) {
                val result = fetchData1.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Omzet(
                            dataArray.getJSONObject(i).getString("kode"),
                            dataArray.getJSONObject(i).getString("nama"),
                            dataArray.getJSONObject(i).getString("omzet")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }

        val fetchData2 = FetchData(ApiSales.OMZET_TOTAL)
        if (fetchData2.startFetch()) {
            if (fetchData2.onComplete()) {
                val result: String = fetchData2.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    totalOmzet.text = "Rp. " + formatNumber.format(dataObject.getString("SUM(omzet)").toInt()) + ",00"
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityOmzet, ActivityBeranda::class.java))
        finish()
    }
}