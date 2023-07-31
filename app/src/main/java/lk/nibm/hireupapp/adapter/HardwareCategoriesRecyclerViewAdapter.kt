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
import lk.nibm.hireupapp.activities.HardwareCategories
import lk.nibm.hireupapp.activities.InsideHardware
import lk.nibm.hireupapp.model.HardwareCategoriesData

class HardwareCategoriesRecyclerViewAdapter(
    private val getActivity: HardwareCategories,
    private val hardwareCategoriesItemList: MutableList<HardwareCategoriesData>
) : RecyclerView.Adapter<HardwareCategoriesRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: HardwareCategoriesData) {
            name.text = item.name
            Glide.with(itemView)
                .load(item.image)
                .into(image)
        }

        val image: ImageView = itemView.findViewById(R.id.hardware_category_image)
        val name: TextView = itemView.findViewById(R.id.hardware_category_name)
        val cardView: CardView = itemView.findViewById(R.id.hardware_category_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shop_home_hardwares_categories, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hardwareCategoriesItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = hardwareCategoriesItemList[position]
        holder.bind(item)

        // Register the click event for the card view
        holder.cardView.setOnClickListener {
            val intent = Intent(getActivity, InsideHardware::class.java)
            intent.putExtra("HARDWARE_CATEGORY_NAME", hardwareCategoriesItemList[position].name)
            intent.putExtra("HARDWARE_CATEGORY_ID", hardwareCategoriesItemList[position].id)
            getActivity.startActivity(intent)

            // Pass the click event to the handleCategoryClick function in InsideHardware
            hardwareCategoriesItemList[position].name?.let { it1 ->
                (getActivity as? InsideHardware)?.handleCategoryClick(
                    it1,
                    hardwareCategoriesItemList[position].id
                )
            }
        }
    }
}
