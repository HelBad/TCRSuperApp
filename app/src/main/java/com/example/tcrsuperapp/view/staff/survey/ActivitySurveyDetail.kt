package com.example.tcrsuperapp.view.staff.survey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiStaff
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_survey_detail.*
import org.json.JSONObject

class ActivitySurveyDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_survey_detail)

        getData()
    }

    private fun getData() {
        val fetchData = FetchData(ApiStaff.SURVEY_DETAIL +
                "?kode=" + intent.getStringExtra("kode").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    idDetailSurvey1.text = dataObject.getString("nama")
                    idDetailSurvey2.text = dataObject.getString("perusahaan")
                    idDetailSurvey3.text = dataObject.getString("pic")
                    idDetailSurvey4.text = dataObject.getString("waktu")
                    idDetailSurvey5.text = dataObject.getString("lokasi")

                    if(dataObject.getString("survey_1") == "5") {
                        nilaiDetailSurvey11.visibility = View.VISIBLE
                        nilaiDetailSurvey12.visibility = View.GONE
                        nilaiDetailSurvey13.visibility = View.VISIBLE
                        nilaiDetailSurvey14.visibility = View.GONE
                        nilaiDetailSurvey15.visibility = View.GONE
                        nilaiDetailSurvey16.visibility = View.VISIBLE
                    } else if(dataObject.getString("survey_1") == "3") {
                        nilaiDetailSurvey11.visibility = View.VISIBLE
                        nilaiDetailSurvey12.visibility = View.GONE
                        nilaiDetailSurvey13.visibility = View.GONE
                        nilaiDetailSurvey14.visibility = View.VISIBLE
                        nilaiDetailSurvey15.visibility = View.VISIBLE
                        nilaiDetailSurvey16.visibility = View.GONE
                    } else if(dataObject.getString("survey_1") == "1") {
                        nilaiDetailSurvey11.visibility = View.GONE
                        nilaiDetailSurvey12.visibility = View.VISIBLE
                        nilaiDetailSurvey13.visibility = View.VISIBLE
                        nilaiDetailSurvey14.visibility = View.GONE
                        nilaiDetailSurvey15.visibility = View.VISIBLE
                        nilaiDetailSurvey16.visibility = View.GONE
                    } else {
                        nilaiDetailSurvey11.visibility = View.VISIBLE
                        nilaiDetailSurvey12.visibility = View.GONE
                        nilaiDetailSurvey13.visibility = View.VISIBLE
                        nilaiDetailSurvey14.visibility = View.GONE
                        nilaiDetailSurvey15.visibility = View.VISIBLE
                        nilaiDetailSurvey16.visibility = View.GONE
                    }

                    if(dataObject.getString("survey_2") == "5") {
                        nilaiDetailSurvey21.visibility = View.VISIBLE
                        nilaiDetailSurvey22.visibility = View.GONE
                        nilaiDetailSurvey23.visibility = View.VISIBLE
                        nilaiDetailSurvey24.visibility = View.GONE
                        nilaiDetailSurvey25.visibility = View.GONE
                        nilaiDetailSurvey26.visibility = View.VISIBLE
                    } else if(dataObject.getString("survey_2") == "3") {
                        nilaiDetailSurvey21.visibility = View.VISIBLE
                        nilaiDetailSurvey22.visibility = View.GONE
                        nilaiDetailSurvey23.visibility = View.GONE
                        nilaiDetailSurvey24.visibility = View.VISIBLE
                        nilaiDetailSurvey25.visibility = View.VISIBLE
                        nilaiDetailSurvey26.visibility = View.GONE
                    } else if(dataObject.getString("survey_2") == "1") {
                        nilaiDetailSurvey21.visibility = View.GONE
                        nilaiDetailSurvey22.visibility = View.VISIBLE
                        nilaiDetailSurvey23.visibility = View.VISIBLE
                        nilaiDetailSurvey24.visibility = View.GONE
                        nilaiDetailSurvey25.visibility = View.VISIBLE
                        nilaiDetailSurvey26.visibility = View.GONE
                    } else {
                        nilaiDetailSurvey21.visibility = View.VISIBLE
                        nilaiDetailSurvey22.visibility = View.GONE
                        nilaiDetailSurvey23.visibility = View.VISIBLE
                        nilaiDetailSurvey24.visibility = View.GONE
                        nilaiDetailSurvey25.visibility = View.VISIBLE
                        nilaiDetailSurvey26.visibility = View.GONE
                    }

                    if(dataObject.getString("survey_3") == "5") {
                        nilaiDetailSurvey31.visibility = View.VISIBLE
                        nilaiDetailSurvey32.visibility = View.GONE
                        nilaiDetailSurvey33.visibility = View.VISIBLE
                        nilaiDetailSurvey34.visibility = View.GONE
                        nilaiDetailSurvey35.visibility = View.GONE
                        nilaiDetailSurvey36.visibility = View.VISIBLE
                    } else if(dataObject.getString("survey_3") == "3") {
                        nilaiDetailSurvey31.visibility = View.VISIBLE
                        nilaiDetailSurvey32.visibility = View.GONE
                        nilaiDetailSurvey33.visibility = View.GONE
                        nilaiDetailSurvey34.visibility = View.VISIBLE
                        nilaiDetailSurvey35.visibility = View.VISIBLE
                        nilaiDetailSurvey36.visibility = View.GONE
                    } else if(dataObject.getString("survey_3") == "1") {
                        nilaiDetailSurvey31.visibility = View.GONE
                        nilaiDetailSurvey32.visibility = View.VISIBLE
                        nilaiDetailSurvey33.visibility = View.VISIBLE
                        nilaiDetailSurvey34.visibility = View.GONE
                        nilaiDetailSurvey35.visibility = View.VISIBLE
                        nilaiDetailSurvey36.visibility = View.GONE
                    } else {
                        nilaiDetailSurvey31.visibility = View.VISIBLE
                        nilaiDetailSurvey32.visibility = View.GONE
                        nilaiDetailSurvey33.visibility = View.VISIBLE
                        nilaiDetailSurvey34.visibility = View.GONE
                        nilaiDetailSurvey35.visibility = View.VISIBLE
                        nilaiDetailSurvey36.visibility = View.GONE
                    }

                    nilaiDetailSurvey4.text = dataObject.getString("survey_4")
                    if(dataObject.getString("survey_5") == "") {
                        nilaiDetailSurvey5.text = "-"
                    } else {
                        nilaiDetailSurvey5.text = dataObject.getString("survey_5")
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}