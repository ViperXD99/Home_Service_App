package lk.nibm.hireupapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.activities.ServiceProviders
import lk.nibm.hireupapp.model.Category

class CategoryAdapter(private val itemList: List<Category>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_service_categories, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.cardView.setOnClickListener {
//            Toast.makeText(getActivity, categoryList[position].name, Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, ServiceProviders::class.java)
            intent.putExtra("CATEGORY_NAME", itemList[position].name)
            intent.putExtra("CATEGORY_ID", itemList[position].id)
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
        val cardView : LinearLayout



        init {
            image = itemView.findViewById(R.id.imgCategory)
            name = itemView.findViewById(R.id.txtCategoryName)
            cardView = itemView.findViewById(R.id.categoryLayout)
        }

        fun bind(item: Category) {
            name.text = item.name
            Glide.with(itemView)
                .load(item.image)
                .into(image)
        }
    }

}