package lk.nibm.hireupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.ChatAdapter

class Chat : AppCompatActivity() {

    private lateinit var chatRecyclerView : RecyclerView
    private var layoutManager : RecyclerView.LayoutManager? = null
    private var adapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        layoutManager = LinearLayoutManager(this)
        chatRecyclerView.layoutManager = layoutManager
        adapter = ChatAdapter()
        chatRecyclerView.adapter = adapter
    }
}