package com.example.tcrsuperapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Stok

class AdapterStok(dataList: ArrayList<Stok>?): RecyclerView.Adapter<AdapterStok.StokViewHolder>() {
    private val dataList: ArrayList<Stok>?

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StokViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_stok, parent, false)
        return StokViewHolder(view)
    }

    override fun onBindViewHolder(holder: StokViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.namaStok.text = dataList!![position].nama_barang
        holder.produkStok.text = "STOK : " + dataList[position].stok_aktif
        holder.merkStok.text = dataList[position].nama_merk + " (" + dataList[position].nama_divisi + ")"
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class StokViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaStok: TextView
        val produkStok: TextView
        val merkStok: TextView

        init {
            namaStok = itemView.findViewById<View>(R.id.namaStok) as TextView
            produkStok = itemView.findViewById<View>(R.id.produkStok) as TextView
            merkStok = itemView.findViewById<View>(R.id.merkStok) as TextView
        }
    }
}