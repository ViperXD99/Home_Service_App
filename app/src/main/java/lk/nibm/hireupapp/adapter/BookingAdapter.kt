package lk.nibm.hireupapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import lk.nibm.hireupapp.R

class BookingAdapter(private val context: Context) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    private val dataSet = 10

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView
        var name: TextView
        var desc: TextView
        var priority: TextView
        var salary: TextView
        var day: TextView
        var card: CardView

        init {
            image = view.findViewById(R.id.image)
            name = view.findViewById(R.id.name)
            desc = view.findViewById(R.id.desc)
            priority = view.findViewById(R.id.priority)
            card = view.findViewById(R.id.card)
            salary = view.findViewById(R.id.salary)
            day = view.findViewById(R.id.day)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booking_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return dataSet
    }
}