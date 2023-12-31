package com.example.tcrsuperapp.view.admin.survey

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.adapter.AdapterSurveyCust
import com.example.tcrsuperapp.api.ApiAdmin
import com.example.tcrsuperapp.model.Customer
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_survey_cust.*
import org.json.JSONObject

class ActivitySurveyCust : AppCompatActivity() {
    lateinit var SP: SharedPreferences
    lateinit var adapter: AdapterSurveyCust
    lateinit var dataArrayList: ArrayList<Customer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_survey_cust)

        (this as AppCompatActivity).setSupportActionBar(toolbarCust2)
        SP = getSharedPreferences("Survey", Context.MODE_PRIVATE)
        loadPerusahaan()

        adapter = AdapterSurveyCust(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerCust.layoutManager = layoutManager
        recyclerCust.adapter = adapter

        backCust1.setOnClickListener {
            finish()
        }
        backCust2.setOnClickListener {
            finish()
        }

        lanjutCust.setOnClickListener {
            if(namaCust.text.toString() == "") {
                toolbarCust2.visibility = View.VISIBLE
                toolbarCust1.visibility = View.GONE
                Toast.makeText(this@ActivitySurveyCust, "Pencarian masih kosong", Toast.LENGTH_SHORT).show()
            } else if(namaCust.text.toString() == intent.getStringExtra("nama").toString()) {
                toolbarCust2.visibility = View.VISIBLE
                toolbarCust1.visibility = View.GONE
                Toast.makeText(this@ActivitySurveyCust, "Pencarian gagal", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ActivitySurveyCust, ActivitySurveyCust::class.java)
                intent.putExtra("nama", namaCust.text.toString())
                startActivity(intent)
                finish()
            }
        }
        batalCust.setOnClickListener {
            namaCust.setText("")
        }
    }

    private fun loadPerusahaan() {
        namaCust.setText(intent.getStringExtra("nama").toString())
        if(namaCust.text.toString() == "null") {
            namaCust.setText("")
            loadCust()
        } else {
            namaCust.setText(intent.getStringExtra("nama").toString())
            searchCust()
        }
    }

    private fun loadCust() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.CUSTOMER)
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Customer(
                            dataArray.getJSONObject(i).getString("kode"),
                            dataArray.getJSONObject(i).getString("perusahaan"),
                            dataArray.getJSONObject(i).getString("alamat"),
                            dataArray.getJSONObject(i).getString("kota"),
                            dataArray.getJSONObject(i).getString("telp")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    private fun searchCust() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.CUSTOMER +
                "?perusahaan=" + intent.getStringExtra("nama").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Customer(
                            dataArray.getJSONObject(i).getString("kode"),
                            dataArray.getJSONObject(i).getString("perusahaan"),
                            dataArray.getJSONObject(i).getString("alamat"),
                            dataArray.getJSONObject(i).getString("kota"),
                            dataArray.getJSONObject(i).getString("telp")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_cari, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.cari) {
            toolbarCust2.visibility = View.GONE
            toolbarCust1.visibility = View.VISIBLE
            loadPerusahaan()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }
}