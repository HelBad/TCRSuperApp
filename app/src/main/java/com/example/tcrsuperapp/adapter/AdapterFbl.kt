package com.example.tcrsuperapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Fbl
import java.text.DecimalFormat
import java.text.NumberFormat

class AdapterFbl(dataList: ArrayList<Fbl>?): RecyclerView.Adapter<AdapterFbl.FblViewHolder>() {
    private val dataList: ArrayList<Fbl>?
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FblViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_fbl, parent, false)
        return FblViewHolder(view)
    }

    override fun onBindViewHolder(holder: FblViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.notaFbl.text = dataList!![position].kodenota
        holder.namaFbl.text = dataList[position].perusahaan
        holder.salesFbl.text = "Sales (Now) : " + dataList[position].nama
        if(dataList[position].umur.toString().toInt() > dataList[position].lama_kredit.toString().toInt()
            && dataList[position].sisa_bayar.toString().toInt() <= dataList[position].kredit_limit.toString().toInt()) {
            holder.topFbl.text = "Umur " + dataList[position].umur + " dari TOP " + dataList[position].lama_kredit
            holder.topFbl.setTextColor(Color.parseColor("#FFFF6333"))
            holder.limitFbl.text = "Piutang " + formatNumber.format(dataList[position].sisa_bayar.toString().toInt()) + ", Limit " + formatNumber.format(dataList[position].kredit_limit.toString().toInt())
            holder.limitFbl.setTextColor(Color.parseColor("#FF239D58"))

            holder.imgFbl.visibility = View.VISIBLE
            holder.statusFbl.visibility = View.VISIBLE
            holder.statusFbl.text = "TOP"
        } else if(dataList[position].umur.toString().toInt() <= dataList[position].lama_kredit.toString().toInt()
            && dataList[position].sisa_bayar.toString().toInt() > dataList[position].kredit_limit.toString().toInt()) {
            holder.topFbl.text = "Umur " + dataList[position].umur + " dari TOP " + dataList[position].lama_kredit
            holder.topFbl.setTextColor(Color.parseColor("#FF239D58"))
            holder.limitFbl.text = "Piutang " + formatNumber.format(dataList[position].sisa_bayar.toString().toInt()) + ", Limit " + formatNumber.format(dataList[position].kredit_limit.toString().toInt())
            holder.limitFbl.setTextColor(Color.parseColor("#FFFF6333"))

            holder.imgFbl.visibility = View.VISIBLE
            holder.statusFbl.visibility = View.VISIBLE
            holder.statusFbl.text = "Limit"
        } else if(dataList[position].umur.toString().toInt() > dataList[position].lama_kredit.toString().toInt()
            && dataList[position].sisa_bayar.toString().toInt() > dataList[position].kredit_limit.toString().toInt()) {
            holder.topFbl.text = "Umur " + dataList[position].umur + " dari TOP " + dataList[position].lama_kredit
            holder.topFbl.setTextColor(Color.parseColor("#FFFF6333"))
            holder.limitFbl.text = "Piutang " + formatNumber.format(dataList[position].sisa_bayar.toString().toInt()) + ", Limit " + formatNumber.format(dataList[position].kredit_limit.toString().toInt())
            holder.limitFbl.setTextColor(Color.parseColor("#FFFF6333"))

            holder.imgFbl.visibility = View.VISIBLE
            holder.statusFbl.visibility = View.VISIBLE
            holder.statusFbl.text = "TOP & Limit"
        } else {
            holder.topFbl.text = "Umur " + dataList[position].umur + " dari TOP " + dataList[position].lama_kredit
            holder.topFbl.setTextColor(Color.parseColor("#FF239D58"))
            holder.limitFbl.text = "Piutang " + formatNumber.format(dataList[position].sisa_bayar.toString().toInt()) + ", Limit " + formatNumber.format(dataList[position].kredit_limit.toString().toInt())
            holder.limitFbl.setTextColor(Color.parseColor("#FF239D58"))

            holder.imgFbl.visibility = View.GONE
            holder.statusFbl.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class FblViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val statusFbl: TextView
        val imgFbl: ImageView
        val notaFbl: TextView
        val namaFbl: TextView
        val salesFbl: TextView
        val topFbl: TextView
        val limitFbl: TextView

        init {
            statusFbl = itemView.findViewById<View>(R.id.statusFbl) as TextView
            imgFbl = itemView.findViewById<View>(R.id.imgFbl) as ImageView
            notaFbl = itemView.findViewById<View>(R.id.notaFbl) as TextView
            namaFbl = itemView.findViewById<View>(R.id.namaFbl) as TextView
            salesFbl = itemView.findViewById<View>(R.id.salesFbl) as TextView
            topFbl = itemView.findViewById<View>(R.id.topFbl) as TextView
            limitFbl = itemView.findViewById<View>(R.id.limitFbl) as TextView
        }
    }
}