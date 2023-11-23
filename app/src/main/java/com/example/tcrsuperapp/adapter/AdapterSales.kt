package com.example.tcrsuperapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Sales

class AdapterSales(dataList: ArrayList<Sales>?): RecyclerView.Adapter<AdapterSales.SalesViewHolder>() {
    private val dataList: ArrayList<Sales>?
    lateinit var SP: SharedPreferences

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_omzet, parent, false)
        return SalesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalesViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.namaOmzet.text = dataList!![position].perusahaan
        holder.nominalOmzet.text = dataList[position].produk_1 + " & " + dataList[position].produk_2

        holder.viewHolder.setOnClickListener { v ->
            SP = v.context.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
            if(SP.getString("level", "").toString() == "Admin") {
                val intent = Intent(v.context,
                    com.example.tcrsuperapp.view.admin.sales.ActivitySalesDetail::class.java)
                intent.putExtra("kode", dataList[position].kode_survey)
                v.context.startActivity(intent)
            } else {
                val intent = Intent(v.context,
                    com.example.tcrsuperapp.view.sales.sales.ActivitySalesDetail::class.java)
                intent.putExtra("kode", dataList[position].kode_survey)
                v.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class SalesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaOmzet: TextView
        val nominalOmzet: TextView
        val viewHolder: View

        init {
            namaOmzet = itemView.findViewById<View>(R.id.namaOmzet) as TextView
            nominalOmzet = itemView.findViewById<View>(R.id.nominalOmzet) as TextView
            viewHolder = itemView
        }
    }
}