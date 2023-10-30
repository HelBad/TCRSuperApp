package com.example.tcrsuperapp.view.sales.survey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tcrsuperapp.R
import kotlinx.android.synthetic.main.sales_activity_survey_akhir.btnAkhir

class ActivitySurveyAkhir : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_survey_akhir)

        btnAkhir.setOnClickListener {
            startActivity(Intent(this@ActivitySurveyAkhir, ActivitySurveyList::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySurveyAkhir, ActivitySurveyList::class.java))
        finish()
    }
}