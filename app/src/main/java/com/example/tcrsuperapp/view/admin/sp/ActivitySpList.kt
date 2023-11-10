package com.example.tcrsuperapp.view.admin.sp

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
import com.example.tcrsuperapp.adapter.AdapterSp
import com.example.tcrsuperapp.api.ApiAdmin
import com.example.tcrsuperapp.model.Sp
import com.example.tcrsuperapp.view.admin.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_sp_list.*
import org.json.JSONObject
import java.util.ArrayList

class ActivitySpList : AppCompatActivity() {
    lateinit var adapter: AdapterSp
    lateinit var dataArrayList: ArrayList<Sp>
    var sp: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_sp_list)

        pilihStatus()

        backList1.setOnClickListener {
            startActivity(Intent(this@ActivitySpList, ActivityBeranda::class.java))
            finish()
        }
        backList2.setOnClickListener {
            startActivity(Intent(this@ActivitySpList, ActivityBeranda::class.java))
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
                Toast.makeText(this@ActivitySpList, "Pencarian masih kosong", Toast.LENGTH_SHORT).show()
            } else if(namaList.text.toString() == intent.getStringExtra("nama").toString()) {
                toolbarList2.visibility = View.VISIBLE
                toolbarList1.visibility = View.GONE
                Toast.makeText(this@ActivitySpList, "Pencarian gagal", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@ActivitySpList, ActivitySpList::class.java)
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
        sp.add("DIPROSES")
        sp.add("SUDAH DIKIRIM")
        sp.add("SELESAI")
        sp.add("DIBATALKAN")
        spinnerList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sp)
        spinnerList.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                statusList.text = sp[position]
                loadData()

                adapter = AdapterSp(dataArrayList)
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
            loadSp()
        } else {
            namaList.setText(intent.getStringExtra("nama").toString())
            toolbarList2.visibility = View.GONE
            toolbarList1.visibility = View.VISIBLE
            searchSp()
        }
    }

    private fun loadSp() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.SP + "?status=" + statusList.text.toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Sp(
                            dataArray.getJSONObject(i).getString("kode_nota"),
                            dataArray.getJSONObject(i).getString("perusahaan"),
                            dataArray.getJSONObject(i).getString("tgl_invoice"),
                            dataArray.getJSONObject(i).getString("nominal"),
                            dataArray.getJSONObject(i).getString("status")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    private fun searchSp() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.SP + "?perusahaan=" + namaList.text.toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Sp(
                            dataArray.getJSONObject(i).getString("kode_nota"),
                            dataArray.getJSONObject(i).getString("perusahaan"),
                            dataArray.getJSONObject(i).getString("tgl_invoice"),
                            dataArray.getJSONObject(i).getString("nominal"),
                            dataArray.getJSONObject(i).getString("status")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySpList, ActivityBeranda::class.java))
        finish()
    }
}