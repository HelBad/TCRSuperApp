package com.example.tcrsuperapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Customer

class AdapterSurveyCust(dataList: ArrayList<Customer>?): RecyclerView.Adapter<AdapterSurveyCust.CustViewHolder>() {
    private val dataList: ArrayList<Customer>?
    lateinit var SP: SharedPreferences
    lateinit var SP1: SharedPreferences

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_customer, parent, false)
        return CustViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.namaCust.text = dataList!![position].perusahaan
        holder.alamatCust.text = dataList[position].alamat + ", " + dataList[position].kota
        if(dataList[position].telp == "") {
            holder.telpCust.text = "-"
        } else {
            holder.telpCust.text = dataList[position].telp
        }

        holder.viewHolder.setOnClickListener { v ->
            SP = v.context.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
            SP1 = v.context.getSharedPreferences("Survey", Context.MODE_PRIVATE)
            if(SP.getString("level", "").toString() == "Admin") {
                val intent = Intent(v.context,
                    com.example.tcrsuperapp.view.admin.survey.ActivitySurveyAwal::class.java)
                val editor = SP1.edit()
                editor.putString("kode", dataList[position].kode)
                editor.putString("perusahaan", dataList[position].perusahaan)
                editor.apply()
                v.context.startActivity(intent)
                (v.context as AppCompatActivity).finish()
            } else if(SP.getString("level", "").toString() == "Staff") {
                val intent = Intent(v.context,
                    com.example.tcrsuperapp.view.staff.survey.ActivitySurveyAwal::class.java)
                val editor = SP1.edit()
                editor.putString("kode", dataList[position].kode)
                editor.putString("perusahaan", dataList[position].perusahaan)
                editor.apply()
                v.context.startActivity(intent)
                (v.context as AppCompatActivity).finish()
            } else {
                val intent = Intent(v.context,
                    com.example.tcrsuperapp.view.sales.survey.ActivitySurveyAwal::class.java)
                val editor = SP1.edit()
                editor.putString("kode", dataList[position].kode)
                editor.putString("perusahaan", dataList[position].perusahaan)
                editor.apply()
                v.context.startActivity(intent)
                (v.context as AppCompatActivity).finish()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class CustViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaCust: TextView
        val alamatCust: TextView
        val telpCust: TextView
        val viewHolder: View

        init {
            namaCust = itemView.findViewById<View>(R.id.namaCust) as TextView
            alamatCust = itemView.findViewById<View>(R.id.alamatCust) as TextView
            telpCust = itemView.findViewById<View>(R.id.telpCust) as TextView
            viewHolder = itemView
        }
    }
}