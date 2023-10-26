package com.example.tcrsuperapp.adapter

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Customer

class AdapterCust(dataList: ArrayList<Customer>?): RecyclerView.Adapter<AdapterCust.CustViewHolder>() {
    private val dataList: ArrayList<Customer>?

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
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class CustViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaCust: TextView
        val alamatCust: TextView
        val telpCust: TextView

        init {
            namaCust = itemView.findViewById<View>(R.id.namaCust) as TextView
            alamatCust = itemView.findViewById<View>(R.id.alamatCust) as TextView
            telpCust = itemView.findViewById<View>(R.id.telpCust) as TextView
        }
    }
}