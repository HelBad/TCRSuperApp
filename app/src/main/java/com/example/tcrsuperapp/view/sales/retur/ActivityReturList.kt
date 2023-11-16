package com.example.tcrsuperapp.view.sales.retur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.adapter.AdapterRetur
import com.example.tcrsuperapp.api.ApiSales
import com.example.tcrsuperapp.model.Retur
import com.example.tcrsuperapp.view.sales.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.sales_activity_retur_list.*
import org.json.JSONObject
import java.util.ArrayList

class ActivityReturList : AppCompatActivity() {
    lateinit var adapter: AdapterRetur
    lateinit var dataArrayList: ArrayList<Retur>
    var retur: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_retur_list)

        pilihStatus()

        backList1.setOnClickListener {
            startActivity(Intent(this@ActivityReturList, ActivityBeranda::class.java))
            finish()
        }
        backList2.setOnClickListener {
            startActivity(Intent(this@ActivityReturList, ActivityBeranda::class.java))
            finish()
        }


        cariList.setOnClickListener {
            toolbarList2.visibility = View.GONE
            toolbarList1.visibility = View.VISIBLE
            loadData()
        }
        lanjutList.setOnClickListener {
            if(namaList.text.toString() == "") {
                toolbarList2.visibility = View.VISIBLE
                toolbarList1.visibility = View.GONE
                Toast.makeText(this@ActivityReturList, "Pencarian masih kosong", Toast.LENGTH_SHORT).show()
            } else if(namaList.text.toString() == intent.getStringExtra("nama").toString()) {
                toolbarList2.visibility = View.VISIBLE
                toolbarList1.visibility = View.GONE
                Toast.makeText(this@ActivityReturList, "Pencarian gagal", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ActivityReturList, ActivityReturList::class.java)
                intent.putExtra("nama", namaList.text.toString())
                startActivity(intent)
                finish()
            }
        }
        batalList.setOnClickListener {
            namaList.setText("")
        }
    }

    private fun pilihStatus() {
        retur.add("DIPROSES")
        retur.add("SELESAI")
        retur.add("DIBATALKAN")
        spinnerList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, retur)
        spinnerList.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                statusList.text = retur[position]
                loadData()

                adapter = AdapterRetur(dataArrayList)
                val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
                recyclerList.layoutManager = layoutManager
                recyclerList.adapter = adapter
            }
        }
    }

    private fun loadData() {
        namaList.setText(intent.getStringExtra("nama").toString())
        if(namaList.text.toString() == "null") {
            namaList.setText("")
            loadRetur()
        } else {
            namaList.setText(intent.getStringExtra("nama").toString())
            toolbarList2.visibility = View.GONE
            toolbarList1.visibility = View.VISIBLE
            searchRetur()
        }
    }

    private fun loadRetur() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiSales.RETUR + "?status=" + statusList.text.toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Retur(
                            dataArray.getJSONObject(i).getString("id_retur"),
                            dataArray.getJSONObject(i).getString("perusahaan"),
                            dataArray.getJSONObject(i).getString("no_retur"),
                            dataArray.getJSONObject(i).getString("create_date"),
                            dataArray.getJSONObject(i).getString("status")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    private fun searchRetur() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiSales.RETUR + "?perusahaan=" + namaList.text.toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Retur(
                            dataArray.getJSONObject(i).getString("id_retur"),
                            dataArray.getJSONObject(i).getString("perusahaan"),
                            dataArray.getJSONObject(i).getString("no_retur"),
                            dataArray.getJSONObject(i).getString("create_date"),
                            dataArray.getJSONObject(i).getString("status")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityReturList, ActivityBeranda::class.java))
        finish()
    }
}