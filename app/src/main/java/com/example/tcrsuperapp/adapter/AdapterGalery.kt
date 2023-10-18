package com.example.tcrsuperapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Galery
import com.squareup.picasso.Picasso

class AdapterGalery(dataList: ArrayList<Galery>?): RecyclerView.Adapter<AdapterGalery.GaleryViewHolder>() {
    private val dataList: ArrayList<Galery>?

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GaleryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_galery, parent, false)
        return GaleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GaleryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.namaGalery.text = dataList!![position].title
        Picasso.get().load(dataList[position].img).into(holder.imgGalery)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class GaleryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaGalery: TextView
        val imgGalery: ImageView

        init {
            namaGalery = itemView.findViewById<View>(R.id.namaGalery) as TextView
            imgGalery = itemView.findViewById<View>(R.id.imgGalery) as ImageView
        }
    }
}