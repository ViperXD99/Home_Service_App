package lk.nibm.hireupapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.activities.InsideHardware
import lk.nibm.hireupapp.activities.ServiceProviders
import lk.nibm.hireupapp.common.ShopDataManager
import lk.nibm.hireupapp.model.Hardware

class HardwareAdapter(private val itemList: MutableList<Hardware>) : RecyclerView.Adapter<HardwareAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop_home_hardwares, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.card.setOnClickListener {
//            Toast.makeText(holder.itemView.context, item.name, Toast.LENGTH_SHORT).show()
            ShopDataManager.setShop(item)
            val intent = Intent(holder.itemView.context, InsideHardware::class.java)
            holder.itemView.context.startActivity(intent)
        }
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var name: TextView
        private var hardwareAddress: TextView
        var card : CardView



        init {
            image = itemView.findViewById(R.id.hardware_image)
            name = itemView.findViewById(R.id.hardware_name)
            hardwareAddress = itemView.findViewById(R.id.hardware_address)
            card = itemView.findViewById(R.id.hardware_card)

        }

        fun bind(item: Hardware) {
            hardwareAddress.text =item.address+"," + item.city+ "," + item.district
            name.text = item.name
            Glide.with(itemView)
                .load(item.image)
                .into(image)
        }
    }

}