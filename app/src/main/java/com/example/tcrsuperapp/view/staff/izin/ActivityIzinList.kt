package com.example.tcrsuperapp.view.staff.izin

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
import com.example.tcrsuperapp.adapter.AdapterIzin
import com.example.tcrsuperapp.api.ApiStaff
import com.example.tcrsuperapp.model.Izin
import com.example.tcrsuperapp.view.staff.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_izin_list.*
import org.json.JSONObject

class ActivityIzinList : AppCompatActivity() {
    lateinit var adapter: AdapterIzin
    lateinit var SP: SharedPreferences
    lateinit var dataArrayList: ArrayList<Izin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_izin_list)

        (this as AppCompatActivity).setSupportActionBar(toolbarList)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        loadIzin()

        adapter = AdapterIzin(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerList.layoutManager = layoutManager
        recyclerList.adapter = adapter

        backList.setOnClickListener {
            startActivity(Intent(this@ActivityIzinList, ActivityBeranda::class.java))
            finish()
        }
    }

    private fun loadIzin() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiStaff.IZIN + "?kode_pengguna=" + SP.getString("username", "").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Izin(
                            dataArray.getJSONObject(i).getString("kode_izin"),
                            dataArray.getJSONObject(i).getString("kode_pengguna"),
                            dataArray.getJSONObject(i).getString("nama"),
                            dataArray.getJSONObject(i).getString("waktu_awal"),
                            dataArray.getJSONObject(i).getString("waktu_akhir"),
                            dataArray.getJSONObject(i).getString("status")
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
            startActivity(Intent(this@ActivityIzinList, ActivityIzin::class.java))
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityIzinList, ActivityBeranda::class.java))
        finish()
    }
}