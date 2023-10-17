package com.example.tcrsuperapp.view.admin.survey

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.tcrsuperapp.R
import kotlinx.android.synthetic.main.admin_activity_survey_awal.*

class ActivitySurveyAwal : AppCompatActivity() {
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_survey_awal)

        SP = getSharedPreferences("Survey", Context.MODE_PRIVATE)

        btnAwal.setOnClickListener {
            if(tokoAwal.text.toString() == "" && namaAwal.text.toString() == "") {
                Toast.makeText(this@ActivitySurveyAwal, "Lengkapi data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@ActivitySurveyAwal, "Berhasil", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this@ActivitySurveyAwal, ActivitySurvey::class.java))
//                finish()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySurveyAwal, ActivitySurveyList::class.java))
        finish()
    }
}