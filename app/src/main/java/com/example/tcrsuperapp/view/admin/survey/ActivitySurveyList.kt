package com.example.tcrsuperapp.view.admin.survey

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.adapter.AdapterSurvey
import com.example.tcrsuperapp.api.ApiAdmin
import com.example.tcrsuperapp.model.Survey
import com.example.tcrsuperapp.view.admin.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_survey_list.*
import org.json.JSONObject
import java.util.ArrayList

class ActivitySurveyList : AppCompatActivity() {
    lateinit var adapter: AdapterSurvey
    lateinit var dataArrayList: ArrayList<Survey>
    var survey: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_survey_list)

        pilihStatus()

        backList.setOnClickListener {
            startActivity(Intent(this@ActivitySurveyList, ActivityBeranda::class.java))
            finish()
        }
        tambahList.setOnClickListener {
            startActivity(Intent(this@ActivitySurveyList, ActivitySurveyAwal::class.java))
            finish()
        }
    }

    private fun pilihStatus() {
        survey.add("SEMUA PELAKSANA")
        val fetchData = FetchData(ApiAdmin.SURVEY_SEARCH)
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataArray = data.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        survey.add(dataArray.getJSONObject(i).getString("nama"))
                    }
                } catch (t: Throwable) { }
            }
        }

        spinnerList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, survey)
        spinnerList.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                namaList.text = survey[position]
                loadData()

                adapter = AdapterSurvey(dataArrayList)
                val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
                recyclerList.layoutManager = layoutManager
                recyclerList.adapter = adapter
            }
        }
    }

    private fun loadData() {
        if(namaList.text.toString() == "SEMUA PELAKSANA") {
            loadSurvey()
        } else {
            searchSurvey()
        }
    }

    private fun loadSurvey() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.SURVEY)
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

    private fun searchSurvey() {
        dataArrayList = ArrayList()
        val fetchData = FetchData(ApiAdmin.SURVEY + "?nama=" + namaList.text.toString())
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

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySurveyList, ActivityBeranda::class.java))
        finish()
    }
}