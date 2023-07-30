package lk.nibm.hireupapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.model.TopRatedSP

class TopRatedAdapter(private val topSpList: List<TopRatedSP>) : RecyclerView.Adapter<TopRatedAdapter.ViewHolder>() {
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var imageURL : ImageView
        var spName : TextView
        var spCategory : TextView
        var spRating : TextView
        var spReview : TextView
        var spProfilePhoto : ImageView


        init {
            spName = itemView.findViewById(R.id.spName)
            spCategory = itemView.findViewById(R.id.spCategory)
            spRating = itemView.findViewById(R.id.spRating)
            spReview = itemView.findViewById(R.id.spReview)
            spProfilePhoto = itemView.findViewById(R.id.spProfilePhoto)
        }
        fun bind(item: TopRatedSP) {
            spName.text = item.spName
            spReview.text = item.ratingCount
            spRating.text = item.ratingValue.toString()
            Glide.with(itemView)
                .load(item.spImageURL)
                .into(spProfilePhoto)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.top_rated_service_provider_card, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopRatedAdapter.ViewHolder, position: Int) {
        val item = topSpList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return topSpList.size
    }


}