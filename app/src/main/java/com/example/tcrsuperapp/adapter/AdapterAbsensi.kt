package com.example.tcrsuperapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Absensi
import com.example.tcrsuperapp.view.admin.absensi.ActivityAbsensiDetail

class AdapterAbsensi(dataList: ArrayList<Absensi>?): RecyclerView.Adapter<AdapterAbsensi.AbsensiViewHolder>() {
    private val dataList: ArrayList<Absensi>?

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsensiViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_absensi, parent, false)
        return AbsensiViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbsensiViewHolder, position: Int) {
        holder.namaAbsensi.text = dataList!![position].nama + " (" + dataList!![position].kode_pengguna + ")"
        holder.waktuAbsensi.text = dataList[position].tanggal + ", " + dataList[position].waktu +
                " (" + dataList!![position].status + ")"
        holder.statusAbsensi.text = "Status : " + dataList[position].keterangan + " - " + dataList[position].approval
        holder.viewHolder.setOnClickListener { v ->
            val intent = Intent(v.context, ActivityAbsensiDetail::class.java)
            intent.putExtra("kode", dataList[position].kode)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class AbsensiViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaAbsensi: TextView
        val waktuAbsensi: TextView
        val statusAbsensi: TextView
        val viewHolder: View

        init {
            namaAbsensi = itemView.findViewById<View>(R.id.namaAbsensi) as TextView
            waktuAbsensi = itemView.findViewById<View>(R.id.waktuAbsensi) as TextView
            statusAbsensi = itemView.findViewById<View>(R.id.statusAbsensi) as TextView
            viewHolder = itemView
        }
    }
}