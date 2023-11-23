package com.example.tcrsuperapp.view.sales.sales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.adapter.AdapterSales
import com.example.tcrsuperapp.api.ApiSales
import com.example.tcrsuperapp.model.Sales
import com.example.tcrsuperapp.view.sales.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.sales_activity_sales_list.*
import org.json.JSONObject
import java.util.ArrayList

class ActivitySalesList : AppCompatActivity() {
    lateinit var adapter: AdapterSales
    lateinit var dataArrayList: ArrayList<Sales>
    var sales: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_sales_list)

        (this as AppCompatActivity).setSupportActionBar(toolbarList)
        loadSurvey()

        adapter = AdapterSales(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerList.layoutManager = layoutManager
        recyclerList.adapter = adapter

        backList.setOnClickListener {
            startActivity(Intent(this@ActivitySalesList, ActivityBeranda::class.java))
            finish()
        }
    }

    private fun loadSurvey() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiSales.SALES + "?nama=" + intent.getStringExtra("nama").toString())
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_tambah, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.tambah) {
            startActivity(Intent(this@ActivitySalesList, ActivitySalesAwal::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySalesList, ActivityBeranda::class.java))
        finish()
    }
}