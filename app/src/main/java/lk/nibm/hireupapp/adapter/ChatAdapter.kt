package lk.nibm.hireupapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lk.nibm.hireupapp.R

class ChatAdapter() : RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

    private var dataSet = 10

    class ViewHolder(chatView : View) : RecyclerView.ViewHolder(chatView){

        var chat : TextView

        init {
            chat = chatView.findViewById(R.id.chat)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return dataSet
    }
}