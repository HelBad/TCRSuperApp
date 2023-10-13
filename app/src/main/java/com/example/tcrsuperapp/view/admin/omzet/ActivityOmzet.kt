package com.example.tcrsuperapp.view.admin.omzet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.adapter.AdapterOmzet
import com.example.tcrsuperapp.api.ApiAdmin
import com.example.tcrsuperapp.model.Omzet
import com.example.tcrsuperapp.view.admin.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_omzet.*
import org.json.JSONObject

class ActivityOmzet : AppCompatActivity() {
    lateinit var SP: SharedPreferences
    lateinit var adapter: AdapterOmzet
    lateinit var dataArrayList: ArrayList<Omzet>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_omzet)

        SP = applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        loadOmzet()

        adapter = AdapterOmzet(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerOmzet.layoutManager = layoutManager
        recyclerOmzet.adapter = adapter

        backOmzet.setOnClickListener {
            startActivity(Intent(this@ActivityOmzet, ActivityBeranda::class.java))
            finish()
        }
    }

    private fun loadOmzet() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.OMZET)
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Omzet(
                            dataArray.getJSONObject(i).getString("kode"),
                            dataArray.getJSONObject(i).getString("nama"),
                            dataArray.getJSONObject(i).getString("omzet")
                        )
                        dataArrayList.add(newdata)
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityOmzet, ActivityBeranda::class.java))
        finish()
    }
}