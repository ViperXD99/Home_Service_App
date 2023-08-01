package lk.nibm.hireupapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.model.HardwareCategoriesData

class HardwareCategoriesNamesAdapter(private val categoryNames: List<HardwareCategoriesData>) : RecyclerView.Adapter<HardwareCategoriesNamesAdapter.ViewHolder>() {
    private val categories : MutableList<HardwareCategoriesData> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onCategoryClick(category: HardwareCategoriesData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var category: Button = itemView.findViewById(R.id.categoryButton)

        fun bind(item: HardwareCategoriesData) {
            category.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HardwareCategoriesNamesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hardware_categories_name, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HardwareCategoriesNamesAdapter.ViewHolder, position: Int) {
        val item = categoryNames[position]
        val context: Context = holder.itemView.context

        holder.category.setOnClickListener {
            onItemClickListener?.onCategoryClick(item)
        }

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return categoryNames.size
    }
}
