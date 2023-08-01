package lk.nibm.hireupapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.HardwareProductsDataManager
import lk.nibm.hireupapp.model.HardwareProductsData

class HardwareProductsAdapter(private var productData: List<HardwareProductsData>) :
    RecyclerView.Adapter<HardwareProductsAdapter.ViewHolder>() {


    fun setData(data: List<HardwareProductsData>) {
        productData = data
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        private var image: ImageView
        val cardView: CardView = itemView.findViewById(R.id.product_card)

        init {
            name = itemView.findViewById(R.id.product_name)
            image = itemView.findViewById(R.id.product_image)


        }

        fun bind(item: HardwareProductsData) {
            name.text = item.name
            Glide.with(itemView)
                .load(item.productImage)
                .into(image)
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HardwareProductsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_card, parent, false)
        return HardwareProductsAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HardwareProductsAdapter.ViewHolder, position: Int) {
        var item = productData[position]
        holder.cardView.setOnClickListener {
            HardwareProductsDataManager.setHardwareProduct(item)
            val intent = Intent(
                holder.itemView.context,
                lk.nibm.hireupapp.activities.ProductDetails::class.java
            )
            holder.itemView.context.startActivity(intent)
        }
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return productData.size
    }
}