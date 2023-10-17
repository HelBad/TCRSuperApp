package com.example.tcrsuperapp.view.admin.fbl

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
import com.example.tcrsuperapp.adapter.AdapterFbl
import com.example.tcrsuperapp.api.ApiAdmin
import com.example.tcrsuperapp.model.Fbl
import com.example.tcrsuperapp.view.admin.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_fbl.*
import org.json.JSONObject

class ActivityFbl : AppCompatActivity() {
    lateinit var adapter: AdapterFbl
    lateinit var dataArrayList: ArrayList<Fbl>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_fbl)

        (this as AppCompatActivity).setSupportActionBar(toolbarFbl2)
        loadPerusahaan()

        adapter = AdapterFbl(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerFbl.layoutManager = layoutManager
        recyclerFbl.adapter = adapter

        backFbl1.setOnClickListener {
            startActivity(Intent(this@ActivityFbl, ActivityBeranda::class.java))
            finish()
        }
        backFbl2.setOnClickListener {
            startActivity(Intent(this@ActivityFbl, ActivityBeranda::class.java))
            finish()
        }

        lanjutFbl.setOnClickListener {
            if(namaFbl.text.toString() == "") {
                toolbarFbl2.visibility = View.VISIBLE
                toolbarFbl1.visibility = View.GONE
                Toast.makeText(this@ActivityFbl, "Pencarian masih kosong", Toast.LENGTH_SHORT).show()
            } else if(namaFbl.text.toString() == intent.getStringExtra("nama").toString()) {
                toolbarFbl2.visibility = View.VISIBLE
                toolbarFbl1.visibility = View.GONE
                Toast.makeText(this@ActivityFbl, "Pencarian gagal", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ActivityFbl, ActivityFbl::class.java)
                intent.putExtra("nama", namaFbl.text.toString())
                startActivity(intent)
                finish()
            }
        }
        batalFbl.setOnClickListener {
            namaFbl.setText("")
        }
    }

    private fun loadPerusahaan() {
        namaFbl.setText(intent.getStringExtra("nama").toString())
        if(namaFbl.text.toString() == "null") {
            namaFbl.setText("")
            loadFbl()
        } else {
            namaFbl.setText(intent.getStringExtra("nama").toString())
            searchFbl()
        }
    }

    private fun loadFbl() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.FBL)
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Fbl(
                            dataArray.getJSONObject(i).getString("kodenota"),
                            dataArray.getJSONObject(i).getString("perusahaan"),
                            dataArray.getJSONObject(i).getString("kredit_limit"),
                            dataArray.getJSONObject(i).getString("sisa_bayar"),
                            dataArray.getJSONObject(i).getString("lama_kredit"),
                            dataArray.getJSONObject(i).getString("umur"),
                            dataArray.getJSONObject(i).getString("nama")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    private fun searchFbl() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.FBL +
                "?perusahaan=" + intent.getStringExtra("nama").toString() +
                "&nama=" + intent.getStringExtra("nama").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Fbl(
                            dataArray.getJSONObject(i).getString("kodenota"),
                            dataArray.getJSONObject(i).getString("perusahaan"),
                            dataArray.getJSONObject(i).getString("kredit_limit"),
                            dataArray.getJSONObject(i).getString("sisa_bayar"),
                            dataArray.getJSONObject(i).getString("lama_kredit"),
                            dataArray.getJSONObject(i).getString("umur"),
                            dataArray.getJSONObject(i).getString("nama")
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
            toolbarFbl2.visibility = View.GONE
            toolbarFbl1.visibility = View.VISIBLE
            loadPerusahaan()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityFbl, ActivityBeranda::class.java))
        finish()
    }
}