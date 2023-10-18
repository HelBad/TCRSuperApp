package com.example.tcrsuperapp.view.admin.survey

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.*
import com.example.tcrsuperapp.R
import kotlinx.android.synthetic.main.admin_activity_survey_akhir.*

class ActivitySurveyAkhir : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_survey_akhir)

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