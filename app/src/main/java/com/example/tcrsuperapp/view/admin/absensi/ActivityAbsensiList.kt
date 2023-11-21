package com.example.tcrsuperapp.view.admin.absensi

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.adapter.AdapterAbsensi
import com.example.tcrsuperapp.api.ApiAdmin
import com.example.tcrsuperapp.model.Absensi
import com.example.tcrsuperapp.view.admin.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_absensi_list.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar

class ActivityAbsensiList : AppCompatActivity() {
    lateinit var SP: SharedPreferences
    lateinit var adapter: AdapterAbsensi
    lateinit var dataArrayList: ArrayList<Absensi>
    var formatDate = SimpleDateFormat("YYYY-MM-dd")
    val kalender = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_absensi_list)

        (this as AppCompatActivity).setSupportActionBar(toolbarList2)
        SP = applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        loadTgl()

        adapter = AdapterAbsensi(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerList.layoutManager = layoutManager
        recyclerList.adapter = adapter

        backList1.setOnClickListener {
            startActivity(Intent(this@ActivityAbsensiList, ActivityBeranda::class.java))
            finish()
        }
        backList2.setOnClickListener {
            startActivity(Intent(this@ActivityAbsensiList, ActivityBeranda::class.java))
            finish()
        }

        tglList.setOnClickListener {
            setWaktu()
        }
        lanjutList.setOnClickListener {
            if(tglList.text.toString() == "") {
                toolbarList2.visibility = View.VISIBLE
                toolbarList1.visibility = View.GONE
                Toast.makeText(this@ActivityAbsensiList, "Pencarian masih kosong", Toast.LENGTH_SHORT).show()
            } else if(tglList.text.toString() == intent.getStringExtra("tgl").toString()) {
                toolbarList2.visibility = View.VISIBLE
                toolbarList1.visibility = View.GONE
                Toast.makeText(this@ActivityAbsensiList, "Pencarian gagal", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ActivityAbsensiList, ActivityAbsensiList::class.java)
                intent.putExtra("tgl", tglList.text.toString())
                startActivity(intent)
                finish()
            }
        }
        batalList.setOnClickListener {
            tglList.text = ""
        }

        menungguList.setOnClickListener {
            val intent = Intent(this@ActivityAbsensiList, ActivityAbsensiList::class.java)
            intent.putExtra("approval", "MENUNGGU")
            startActivity(intent)
            finish()
        }
        setujuList.setOnClickListener {
            val intent = Intent(this@ActivityAbsensiList, ActivityAbsensiList::class.java)
            intent.putExtra("approval", "DISETUJUI")
            startActivity(intent)
            finish()
        }
        tolakList.setOnClickListener {
            val intent = Intent(this@ActivityAbsensiList, ActivityAbsensiList::class.java)
            intent.putExtra("approval", "DITOLAK")
            startActivity(intent)
            finish()
        }
    }

    private fun loadTgl() {
        tglList.text = intent.getStringExtra("tgl").toString()
        if(tglList.text.toString() == "null") {
            laybarList3.visibility = View.VISIBLE
            tglList.text = ""

            if(intent.getStringExtra("approval").toString() == "DISETUJUI") {
                loadAbsensi()
                menungguList.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                setujuList.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
                tolakList.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            } else if(intent.getStringExtra("approval").toString() == "DITOLAK") {
                loadAbsensi()
                menungguList.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                setujuList.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                tolakList.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            } else {
                loadAbsensi()
                menungguList.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
                setujuList.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                tolakList.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            }
        } else {
            laybarList3.visibility = View.GONE
            tglList.text = intent.getStringExtra("tgl").toString()
            searchAbsensi()
        }
    }

    private fun setWaktu() {
        val tanggal = DatePickerDialog(this, {
                view, year, month, dayOfMonth -> val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            tglList.text = formatDate.format(selectedDate.time)
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH))
        tanggal.show()
    }

    private fun loadAbsensi() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.ABSENSI +
                "?approval=" + intent.getStringExtra("approval").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Absensi(
                            dataArray.getJSONObject(i).getString("kode"),
                            dataArray.getJSONObject(i).getString("kode_pengguna"),
                            dataArray.getJSONObject(i).getString("tanggal"),
                            dataArray.getJSONObject(i).getString("waktu"),
                            dataArray.getJSONObject(i).getString("keterangan"),
                            dataArray.getJSONObject(i).getString("status"),
                            dataArray.getJSONObject(i).getString("approval"),
                            dataArray.getJSONObject(i).getString("nama")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    private fun searchAbsensi() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.ABSENSI +
                "?tanggal=" + intent.getStringExtra("tgl").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Absensi(
                            dataArray.getJSONObject(i).getString("kode"),
                            dataArray.getJSONObject(i).getString("kode_pengguna"),
                            dataArray.getJSONObject(i).getString("tanggal"),
                            dataArray.getJSONObject(i).getString("waktu"),
                            dataArray.getJSONObject(i).getString("keterangan"),
                            dataArray.getJSONObject(i).getString("status"),
                            dataArray.getJSONObject(i).getString("approval"),
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
            toolbarList2.visibility = View.GONE
            toolbarList1.visibility = View.VISIBLE
            loadTgl()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityAbsensiList, ActivityBeranda::class.java))
        finish()
    }
}