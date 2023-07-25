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
import lk.nibm.hireupapp.activities.AllHardwares
import lk.nibm.hireupapp.activities.ServiceProviders
import lk.nibm.hireupapp.model.Hardware

class HardwareRecyclerViewAdapter(private val getActivity: AllHardwares, private val hardwareList: MutableList<Hardware>): RecyclerView.Adapter<HardwareRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        fun bind(item: Hardware) {
            name.text = item.name
            Glide.with(itemView)
                .load(item.image)
                .into(image)

        }


        val image : ImageView = itemView.findViewById(R.id.imgCategory)
        val name : TextView = itemView.findViewById(R.id.txtCategoryName)
        val cardView : CardView = itemView.findViewById(R.id.categoryCard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hardwareList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = hardwareList[position]
        holder.cardView.setOnClickListener {
//            Toast.makeText(getActivity, categoryList[position].name, Toast.LENGTH_SHORT).show()
            val intent = Intent(getActivity, ServiceProviders::class.java)
            intent.putExtra("CATEGORY_NAME", hardwareList[position].name)
            intent.putExtra("CATEGORY_ID", hardwareList[position].id)
            getActivity.startActivity(intent)
        }
        holder.bind(item)
    }
}