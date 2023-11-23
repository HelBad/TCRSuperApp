package com.example.tcrsuperapp.view.admin.sales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.adapter.AdapterSales
import com.example.tcrsuperapp.api.ApiAdmin
import com.example.tcrsuperapp.model.Sales
import com.example.tcrsuperapp.view.admin.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_sales_list.*
import org.json.JSONObject
import java.util.ArrayList

class ActivitySalesList : AppCompatActivity() {
    lateinit var adapter: AdapterSales
    lateinit var dataArrayList: ArrayList<Sales>
    var sales: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_sales_list)

        pilihStatus()
        backList.setOnClickListener {
            startActivity(Intent(this@ActivitySalesList, ActivityBeranda::class.java))
            finish()
        }
        tambahList.setOnClickListener {
            startActivity(Intent(this@ActivitySalesList, ActivitySalesAwal::class.java))
            finish()
        }
    }

    private fun pilihStatus() {
        sales.add("SEMUA PELAKSANA")
        val fetchData = FetchData(ApiAdmin.SALES_SEARCH)
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        sales.add(dataArray.getJSONObject(i).getString("nama"))
                    }
                } catch (t: Throwable) { }
            }
        }

        spinnerList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sales)
        spinnerList.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                namaList.text = sales[position]
                loadData()

                adapter = AdapterSales(dataArrayList)
                val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
                recyclerList.layoutManager = layoutManager
                recyclerList.adapter = adapter
            }
        }
    }

    private fun loadData() {
        if(namaList.text.toString() == "SEMUA PELAKSANA") {
            loadSales()
        } else {
            searchSales()
        }
    }

    private fun loadSales() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.SALES)
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Sales(
                            dataArray.getJSONObject(i).getString("kode_survey"),
                            dataArray.getJSONObject(i).getString("perusahaan"),
                            dataArray.getJSONObject(i).getString("produk_1"),
                            dataArray.getJSONObject(i).getString("produk_2")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    private fun searchSales() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.SALES + "?nama=" + namaList.text.toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Sales(
                            dataArray.getJSONObject(i).getString("kode_survey"),
                            dataArray.getJSONObject(i).getString("perusahaan"),
                            dataArray.getJSONObject(i).getString("produk_1"),
                            dataArray.getJSONObject(i).getString("produk_2")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySalesList, ActivityBeranda::class.java))
        finish()
    }
}