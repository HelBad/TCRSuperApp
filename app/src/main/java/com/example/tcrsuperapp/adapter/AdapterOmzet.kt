package com.example.tcrsuperapp.adapter

import android.annotation.SuppressLint
import android.content.*
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Omzet
import java.text.DecimalFormat
import java.text.NumberFormat

class AdapterOmzet(dataList: ArrayList<Omzet>?): RecyclerView.Adapter<AdapterOmzet.OmzetViewHolder>() {
    private val dataList: ArrayList<Omzet>?
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OmzetViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_omzet, parent, false)
        return OmzetViewHolder(view)
    }

    override fun onBindViewHolder(holder: OmzetViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.namaOmzet.text = dataList!![position].nama
        holder.nominalOmzet.text = "Rp. " + formatNumber.format(dataList[position].omzet!!.toInt()) + ",00"
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class OmzetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaOmzet: TextView
        val nominalOmzet: TextView

        init {
            namaOmzet = itemView.findViewById<View>(R.id.namaOmzet) as TextView
            nominalOmzet = itemView.findViewById<View>(R.id.nominalOmzet) as TextView
        }
    }
}