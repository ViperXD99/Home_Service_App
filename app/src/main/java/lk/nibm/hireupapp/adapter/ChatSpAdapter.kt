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
import lk.nibm.hireupapp.activities.Chat
import lk.nibm.hireupapp.model.ChatSp
import lk.nibm.hireupapp.model.ServiceProviders
import lk.nibm.hireupapp.model.User

class ChatSpAdapter(private val chatSpList: List<ChatSp>, private val serviceNameList : List<String>, private val providerList : List<ServiceProviders>) : RecyclerView.Adapter<ChatSpAdapter.ViewHolder>(){

    //private var dataSet = 10

    class ViewHolder(chatView : View) : RecyclerView.ViewHolder(chatView){
        var cardView : CardView
        var spImage : ImageView
        var spName : TextView
        var spCategory : TextView
        init {
            cardView = chatView.findViewById(R.id.cardView)
            spImage = chatView.findViewById(R.id.spImage)
            spName = chatView.findViewById(R.id.spName)
            spCategory = chatView.findViewById(R.id.spCategory)
        }
        fun bind(chatId: ChatSp,serviceName: String, provider: ServiceProviders){
            spName.text = provider.full_name
            spCategory.text = serviceName
           Glide.with(itemView).load(provider.photoURL).into(spImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_sp_card, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val chatSp = chatSpList[position]
        val serviceName = serviceNameList[position]
        val provider = providerList[position]
        //val user = userList[position]
        holder.bind(chatSp,serviceName, provider)


        holder.cardView.setOnClickListener {
                //Toast.makeText(holder.itemView.context, "Clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, Chat::class.java)

            intent.putExtra("providerId", chatSpList[position].providerId) // new for chat
            intent.putExtra("providerName", providerList[position].full_name) // new for chat
            intent.putExtra("providerPhoto", providerList[position].photoURL) // new for chat

            holder.itemView.context.startActivity(intent)



        }
    }

    override fun getItemCount(): Int {
        return chatSpList.size
    }

}