package ru.suleymanovtat.gallery.adapter

import ApiCore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Callback
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_gallery.view.*
import ru.suleymanovtat.gallery.R
import ru.suleymanovtat.gallery.model.Items
import java.lang.Exception
import java.util.*


class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private var locations: ArrayList<Items> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = locations[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations.size
    }

    fun updateData(viewModels: ArrayList<Items>) {
        locations.clear()
        locations.addAll(viewModels)
        notifyDataSetChanged()
    }

    private val clickSubject = PublishSubject.create<String>()
    val clickEvent: Observable<String> = clickSubject


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Callback {

        fun bind(item: Items) {
            itemView.tvName.text = item.name
            ApiCore.picasso!!.load(item.preview).resize(300, 300).into(itemView.imageItem, this)
            itemView.setOnClickListener {
                clickSubject.onNext(item.file)
            }
        }

        override fun onSuccess() {
            itemView.progressBarImageItem.visibility = View.GONE
        }

        override fun onError(e: Exception?) {
            itemView.progressBarImageItem.visibility = View.GONE
        }
    }
}