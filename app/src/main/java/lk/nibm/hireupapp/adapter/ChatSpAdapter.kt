package lk.nibm.hireupapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.activities.Chat

class ChatSpAdapter : RecyclerView.Adapter<ChatSpAdapter.ViewHolder>(){

    private var dataSet = 10

    class ViewHolder(chatView : View) : RecyclerView.ViewHolder(chatView){
        var cardView : CardView
        var spImage : ImageView
        var spName : TextView
        var spLastMessage : TextView
        init {
            cardView = chatView.findViewById(R.id.cardView)
            spImage = chatView.findViewById(R.id.spImage)
            spName = chatView.findViewById(R.id.spName)
            spLastMessage = chatView.findViewById(R.id.spLastMessage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_sp_card, parent, false)
        return ViewHolder(view)
    }

    // TODO: Bind data to the views inside the CardView
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        //cardView = .findViewById(R.id.cardView)
        holder.cardView.setOnClickListener {
                //Toast.makeText(holder.itemView.context, "Clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, Chat::class.java)
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return dataSet
    }

}