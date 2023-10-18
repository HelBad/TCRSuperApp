package com.example.tcrsuperapp.view.admin.survey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.*
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.admin_activity_survey_detail.*

class ActivitySurveyDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_survey_detail)

        dataSurvey()
        ratingSurvey()
    }

    private fun ratingSurvey() {
        nilaiDetailSurvey11.visibility = View.VISIBLE
        nilaiDetailSurvey12.visibility = View.GONE
        nilaiDetailSurvey13.visibility = View.VISIBLE
        nilaiDetailSurvey14.visibility = View.GONE
        nilaiDetailSurvey15.visibility = View.VISIBLE
        nilaiDetailSurvey16.visibility = View.GONE

        nilaiDetailSurvey21.visibility = View.VISIBLE
        nilaiDetailSurvey22.visibility = View.GONE
        nilaiDetailSurvey23.visibility = View.VISIBLE
        nilaiDetailSurvey24.visibility = View.GONE
        nilaiDetailSurvey25.visibility = View.VISIBLE
        nilaiDetailSurvey26.visibility = View.GONE

        nilaiDetailSurvey31.visibility = View.VISIBLE
        nilaiDetailSurvey32.visibility = View.GONE
        nilaiDetailSurvey33.visibility = View.VISIBLE
        nilaiDetailSurvey34.visibility = View.GONE
        nilaiDetailSurvey35.visibility = View.VISIBLE
        nilaiDetailSurvey36.visibility = View.GONE
    }

    private fun dataSurvey() {
        FirebaseDatabase.getInstance().getReference("pengguna").orderByChild("id_pengguna")
            .equalTo(intent.getStringExtra("id_pengguna").toString())
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    for (snapshot1 in datasnapshot.children) {
                        val o = snapshot1.getValue(Pengguna::class.java)
                        idDetailSurvey1.text = o!!.nama
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })

        FirebaseDatabase.getInstance().getReference("customer").child("survey")
            .orderByChild("id_customer").equalTo(intent.getStringExtra("id_customer").toString())
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    for (snapshot1 in datasnapshot.children) {
                        val p = snapshot1.getValue(Customer::class.java)
                        idDetailSurvey2.text = p!!.nama_cust
                        idDetailSurvey3.text = p.jenis_cust + " (" + p.kategori + ")"
                        idDetailSurvey4.text = p.pic
                        idDetailSurvey5.text = p.lokasi_cust
                        idDetailSurvey6.text = p.waktu_cust
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })

        FirebaseDatabase.getInstance().getReference("survey").orderByChild("id_customer")
            .equalTo(intent.getStringExtra("id_customer").toString())
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    for (snapshot2 in datasnapshot.children) {
                        val s = snapshot2.getValue(Survey::class.java)

                        if(s!!.survey_1 == "5") {
                            nilaiDetailSurvey11.visibility = View.VISIBLE
                            nilaiDetailSurvey12.visibility = View.GONE
                            nilaiDetailSurvey13.visibility = View.VISIBLE
                            nilaiDetailSurvey14.visibility = View.GONE
                            nilaiDetailSurvey15.visibility = View.GONE
                            nilaiDetailSurvey16.visibility = View.VISIBLE
                        } else if(s.survey_1 == "3") {
                            nilaiDetailSurvey11.visibility = View.VISIBLE
                            nilaiDetailSurvey12.visibility = View.GONE
                            nilaiDetailSurvey13.visibility = View.GONE
                            nilaiDetailSurvey14.visibility = View.VISIBLE
                            nilaiDetailSurvey15.visibility = View.VISIBLE
                            nilaiDetailSurvey16.visibility = View.GONE
                        } else {
                            nilaiDetailSurvey11.visibility = View.GONE
                            nilaiDetailSurvey12.visibility = View.VISIBLE
                            nilaiDetailSurvey13.visibility = View.VISIBLE
                            nilaiDetailSurvey14.visibility = View.GONE
                            nilaiDetailSurvey15.visibility = View.VISIBLE
                            nilaiDetailSurvey16.visibility = View.GONE
                        }

                        if(s.survey_2 == "5") {
                            nilaiDetailSurvey21.visibility = View.VISIBLE
                            nilaiDetailSurvey22.visibility = View.GONE
                            nilaiDetailSurvey23.visibility = View.VISIBLE
                            nilaiDetailSurvey24.visibility = View.GONE
                            nilaiDetailSurvey25.visibility = View.GONE
                            nilaiDetailSurvey26.visibility = View.VISIBLE
                        } else if(s.survey_2 == "3") {
                            nilaiDetailSurvey21.visibility = View.VISIBLE
                            nilaiDetailSurvey22.visibility = View.GONE
                            nilaiDetailSurvey23.visibility = View.GONE
                            nilaiDetailSurvey24.visibility = View.VISIBLE
                            nilaiDetailSurvey25.visibility = View.VISIBLE
                            nilaiDetailSurvey26.visibility = View.GONE
                        } else {
                            nilaiDetailSurvey21.visibility = View.GONE
                            nilaiDetailSurvey22.visibility = View.VISIBLE
                            nilaiDetailSurvey23.visibility = View.VISIBLE
                            nilaiDetailSurvey24.visibility = View.GONE
                            nilaiDetailSurvey25.visibility = View.VISIBLE
                            nilaiDetailSurvey26.visibility = View.GONE
                        }

                        if(s.survey_3 == "5") {
                            nilaiDetailSurvey31.visibility = View.VISIBLE
                            nilaiDetailSurvey32.visibility = View.GONE
                            nilaiDetailSurvey33.visibility = View.VISIBLE
                            nilaiDetailSurvey34.visibility = View.GONE
                            nilaiDetailSurvey35.visibility = View.GONE
                            nilaiDetailSurvey36.visibility = View.VISIBLE
                        } else if(s.survey_3 == "3") {
                            nilaiDetailSurvey31.visibility = View.VISIBLE
                            nilaiDetailSurvey32.visibility = View.GONE
                            nilaiDetailSurvey33.visibility = View.GONE
                            nilaiDetailSurvey34.visibility = View.VISIBLE
                            nilaiDetailSurvey35.visibility = View.VISIBLE
                            nilaiDetailSurvey36.visibility = View.GONE
                        } else {
                            nilaiDetailSurvey31.visibility = View.GONE
                            nilaiDetailSurvey32.visibility = View.VISIBLE
                            nilaiDetailSurvey33.visibility = View.VISIBLE
                            nilaiDetailSurvey34.visibility = View.GONE
                            nilaiDetailSurvey35.visibility = View.VISIBLE
                            nilaiDetailSurvey36.visibility = View.GONE
                        }

                        nilaiDetailSurvey4.text = s.survey_4
                        if(s.survey_5 == "") {
                            nilaiDetailSurvey5.text = "-"
                        } else {
                            nilaiDetailSurvey5.text = s.survey_5
                        }
                        if(s.foto_survey == "") {
                            imgDetailSurvey.visibility = View.GONE
                        } else {
                            nilaiDetailSurvey6.visibility = View.GONE
                            Picasso.get().load(s.foto_survey).into(imgDetailSurvey)
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    override fun onBackPressed() {
        finish()
    }
}