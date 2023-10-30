package com.example.tcrsuperapp.view.sales.survey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.adapter.AdapterSurvey
import com.example.tcrsuperapp.api.ApiSales
import com.example.tcrsuperapp.model.Survey
import com.example.tcrsuperapp.view.sales.ActivityBeranda
import com.example.tcrsuperapp.view.sales.survey.ActivitySurveyAwal
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.sales_activity_survey_list.*
import org.json.JSONObject
import java.util.ArrayList

class ActivitySurveyList : AppCompatActivity() {
    lateinit var adapter: AdapterSurvey
    lateinit var dataArrayList: ArrayList<Survey>
    var survey: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_survey_list)

        (this as AppCompatActivity).setSupportActionBar(toolbarList)
        loadSurvey()

        adapter = AdapterSurvey(dataArrayList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerList.layoutManager = layoutManager
        recyclerList.adapter = adapter

        backList.setOnClickListener {
            startActivity(Intent(this@ActivitySurveyList, ActivityBeranda::class.java))
            finish()
        }
    }

    private fun loadSurvey() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiSales.SURVEY + "?nama=" + intent.getStringExtra("nama").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val newdata = Survey(
                            dataArray.getJSONObject(i).getString("kode"),
                            dataArray.getJSONObject(i).getString("perusahaan"),
                            dataArray.getJSONObject(i).getString("survey_4"),
                            dataArray.getJSONObject(i).getString("total_rate")
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
            startActivity(Intent(this@ActivitySurveyList, ActivitySurveyAwal::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySurveyList, ActivityBeranda::class.java))
        finish()
    }
}