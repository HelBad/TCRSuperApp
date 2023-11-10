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
import com.example.tcrsuperapp.model.Sp
import java.text.DecimalFormat
import java.text.NumberFormat

class AdapterSp(dataList: ArrayList<Sp>?): RecyclerView.Adapter<AdapterSp.SpViewHolder>() {
    private val dataList: ArrayList<Sp>?
    lateinit var SP: SharedPreferences
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_sp, parent, false)
        return SpViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.custSp.text = dataList!![position].perusahaan
        holder.invoiceSp.text = "INVOICE : " + dataList[position].kode_nota
        holder.tglSp.text = dataList[position].tgl_invoice
        holder.nominalSp.text = "Rp. " + formatNumber.format(dataList[position].nominal!!.toInt()) + ",00"

        holder.viewHolder.setOnClickListener { v ->
            SP = v.context.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
            if(SP.getString("level", "").toString() == "Admin") {
                val intent = Intent(v.context,
                    com.example.tcrsuperapp.view.admin.sp.ActivitySpDetail::class.java)
                intent.putExtra("kode", dataList[position].kode_nota)
                intent.putExtra("status", dataList[position].status)
                v.context.startActivity(intent)
                (v.context as AppCompatActivity).finish()
            } else if(SP.getString("level", "").toString() == "Staff") {
                val intent = Intent(v.context,
                    com.example.tcrsuperapp.view.staff.sp.ActivitySpDetail::class.java)
                intent.putExtra("kode", dataList[position].kode_nota)
                intent.putExtra("status", dataList[position].status)
                v.context.startActivity(intent)
                (v.context as AppCompatActivity).finish()
            } else {

            }
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class SpViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val custSp: TextView
        val invoiceSp: TextView
        val tglSp: TextView
        val nominalSp: TextView
        val viewHolder: View

        init {
            custSp = itemView.findViewById<View>(R.id.custSp) as TextView
            invoiceSp = itemView.findViewById<View>(R.id.invoiceSp) as TextView
            tglSp = itemView.findViewById<View>(R.id.tglSp) as TextView
            nominalSp = itemView.findViewById<View>(R.id.nominalSp) as TextView
            viewHolder = itemView
        }
    }
}