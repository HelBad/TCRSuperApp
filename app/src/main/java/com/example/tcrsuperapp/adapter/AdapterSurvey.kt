package com.example.tcrsuperapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Survey
import java.text.DecimalFormat

class AdapterSurvey(dataList: ArrayList<Survey>?): RecyclerView.Adapter<AdapterSurvey.SurveyViewHolder>() {
    private val dataList: ArrayList<Survey>?

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_survey, parent, false)
        return SurveyViewHolder(view)
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.namaSurvey.text = dataList!![position].perusahaan
        holder.produkSurvey.text = "PRODUK : " + dataList[position].survey_4
        holder.ratingSurvey.text = String.format("%.1f", (dataList[position].total_rate.toString().toFloat() / 3))
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class SurveyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaSurvey: TextView
        val ratingSurvey: TextView
        val produkSurvey: TextView

        init {
            namaSurvey = itemView.findViewById<View>(R.id.namaSurvey) as TextView
            ratingSurvey = itemView.findViewById<View>(R.id.ratingSurvey) as TextView
            produkSurvey = itemView.findViewById<View>(R.id.produkSurvey) as TextView
        }
    }
}