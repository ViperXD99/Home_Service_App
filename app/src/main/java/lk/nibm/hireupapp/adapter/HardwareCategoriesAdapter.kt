package lk.nibm.hireupapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.model.HardwareCategoriesData

class HardwareCategoriesAdapter(private val itemList: MutableList<HardwareCategoriesData>) : RecyclerView.Adapter<HardwareCategoriesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop_home_hardwares_categories, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var name: TextView



        init {
            image = itemView.findViewById(R.id.hardware_category_image)
            name = itemView.findViewById(R.id.hardware_category_name)

        }

        fun bind(item: HardwareCategoriesData) {
            name.text = item.name
            Glide.with(itemView)
                .load(item.image)
                .into(image)
        }
    }

}