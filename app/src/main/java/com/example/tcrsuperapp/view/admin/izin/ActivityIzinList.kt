package com.example.tcrsuperapp.view.admin.izin

import android.content.Intent
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
import com.example.tcrsuperapp.api.ApiAdmin
import com.example.tcrsuperapp.model.Izin
import com.example.tcrsuperapp.view.admin.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_izin_list.*
import org.json.JSONObject

class ActivityIzinList : AppCompatActivity() {
    lateinit var adapter: AdapterIzin
    lateinit var dataArrayList: ArrayList<Izin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_izin_list)

        (this as AppCompatActivity).setSupportActionBar(toolbarList2)
        loadPerusahaan()

        adapter = AdapterIzin(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerList.layoutManager = layoutManager
        recyclerList.adapter = adapter

        backList1.setOnClickListener {
            startActivity(Intent(this@ActivityIzinList, ActivityBeranda::class.java))
            finish()
        }
        backList2.setOnClickListener {
            startActivity(Intent(this@ActivityIzinList, ActivityBeranda::class.java))
            finish()
        }

        lanjutList.setOnClickListener {
            if(namaList.text.toString() == "") {
                toolbarList2.visibility = View.VISIBLE
                toolbarList1.visibility = View.GONE
                Toast.makeText(this@ActivityIzinList, "Pencarian masih kosong", Toast.LENGTH_SHORT).show()
            } else if(namaList.text.toString() == intent.getStringExtra("nama").toString()) {
                toolbarList2.visibility = View.VISIBLE
                toolbarList1.visibility = View.GONE
                Toast.makeText(this@ActivityIzinList, "Pencarian gagal", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ActivityIzinList, ActivityIzinList::class.java)
                intent.putExtra("nama", namaList.text.toString())
                startActivity(intent)
                finish()
            }
        }
        batalList.setOnClickListener {
            namaList.setText("")
        }
    }

    private fun loadPerusahaan() {
        namaList.setText(intent.getStringExtra("nama").toString())
        if(namaList.text.toString() == "null") {
            namaList.setText("")
            loadIzin()
        } else {
            namaList.setText(intent.getStringExtra("nama").toString())
            searchIzin()
        }
    }

    private fun loadIzin() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.IZIN)
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

    private fun searchIzin() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.IZIN +
                "?nama=" + intent.getStringExtra("nama").toString())
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
        menuInflater.inflate(R.menu.bar_tambah_cari, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.cari) {
            toolbarList2.visibility = View.GONE
            toolbarList1.visibility = View.VISIBLE
            loadPerusahaan()
            return true
        }
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