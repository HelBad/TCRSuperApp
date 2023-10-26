package com.example.tcrsuperapp.view.staff.stok

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
import com.example.tcrsuperapp.adapter.AdapterStok
import com.example.tcrsuperapp.api.ApiStaff
import com.example.tcrsuperapp.model.Stok
import com.example.tcrsuperapp.view.staff.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_stok.*
import org.json.JSONObject

class ActivityStok : AppCompatActivity() {
    lateinit var adapter: AdapterStok
    lateinit var dataArrayList: ArrayList<Stok>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_stok)

        (this as AppCompatActivity).setSupportActionBar(toolbarStok2)
        loadPerusahaan()

        adapter = AdapterStok(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerStok.layoutManager = layoutManager
        recyclerStok.adapter = adapter

        backStok1.setOnClickListener {
            startActivity(Intent(this@ActivityStok, ActivityBeranda::class.java))
            finish()
        }
        backStok2.setOnClickListener {
            startActivity(Intent(this@ActivityStok, ActivityBeranda::class.java))
            finish()
        }

        lanjutStok.setOnClickListener {
            if(produkStok.text.toString() == "") {
                toolbarStok2.visibility = View.VISIBLE
                toolbarStok1.visibility = View.GONE
                Toast.makeText(this@ActivityStok, "Pencarian masih kosong", Toast.LENGTH_SHORT).show()
            } else if(produkStok.text.toString() == intent.getStringExtra("nama").toString()) {
                toolbarStok2.visibility = View.VISIBLE
                toolbarStok1.visibility = View.GONE
                Toast.makeText(this@ActivityStok, "Pencarian gagal", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ActivityStok, ActivityStok::class.java)
                intent.putExtra("nama", produkStok.text.toString())
                startActivity(intent)
                finish()
            }
        }
        batalStok.setOnClickListener {
            produkStok.setText("")
        }
    }

    private fun loadPerusahaan() {
        produkStok.setText(intent.getStringExtra("nama").toString())
        if(produkStok.text.toString() == "null") {
            produkStok.setText("")
            loadStok()
        } else {
            produkStok.setText(intent.getStringExtra("nama").toString())
            searchStok()
        }
    }

    private fun loadStok() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiStaff.STOK)
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Stok(
                            dataArray.getJSONObject(i).getString("brg"),
                            dataArray.getJSONObject(i).getString("nama_barang"),
                            dataArray.getJSONObject(i).getString("nama_divisi"),
                            dataArray.getJSONObject(i).getString("nama_merk"),
                            dataArray.getJSONObject(i).getString("stok_aktif")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    private fun searchStok() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiStaff.STOK +
                "?nama_barang=" + intent.getStringExtra("nama").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Stok(
                            dataArray.getJSONObject(i).getString("brg"),
                            dataArray.getJSONObject(i).getString("nama_barang"),
                            dataArray.getJSONObject(i).getString("nama_divisi"),
                            dataArray.getJSONObject(i).getString("nama_merk"),
                            dataArray.getJSONObject(i).getString("stok_aktif")
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
            toolbarStok2.visibility = View.GONE
            toolbarStok1.visibility = View.VISIBLE
            loadPerusahaan()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityStok, ActivityBeranda::class.java))
        finish()
    }
}