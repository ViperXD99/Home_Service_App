package lk.nibm.hireupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
//import com.squareup.picasso.Picasso
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.ChatAdapter
import lk.nibm.hireupapp.model.Message

class Chat : AppCompatActivity() {

private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox : EditText
    private lateinit var sendButton : Button
    private lateinit var messageList : ArrayList<Message>
    private lateinit var messageAdapter: ChatAdapter
    private lateinit var mDbRef: DatabaseReference
    private lateinit var serviceProviderName : TextView
    private lateinit var providerProfilePhoto : ImageView
    private lateinit var backButton : ImageView

    var receiveRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        backButton = findViewById(R.id.backButton)
        serviceProviderName = findViewById(R.id.serviceProviderName)
        providerProfilePhoto = findViewById(R.id.recImage)

        val receiverId = intent.getStringExtra("providerId")
        val providerName = intent.getStringExtra("providerName")
        val providerPhoto = intent.getStringExtra("providerPhoto")

        serviceProviderName.text = providerName
        Glide.with(this)
            .load(providerPhoto)
            .into(providerProfilePhoto)
        // Load the profile photo of the service provider
     //   Picasso.get().load(providerPhoto).into(providerProfilePhoto)
        //providerPhoto?.let { providerProfilePhoto.setImageResource(it.toInt()) }

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()
        senderRoom = receiverId + senderUid
        receiveRoom = senderUid + receiverId

        messageRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)
        messageList = ArrayList()
        messageAdapter = ChatAdapter(this, messageList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        mDbRef.child("Chat").child("Chats").child(senderRoom!!).child("messages").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                messageList.clear()
                for (postSnapshot in snapshot.children) {

                    val message = postSnapshot.getValue(Message::class.java)
                    messageList.add(message!!)
                }
                messageAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val currentMessage = Message(message, senderUid)

            // adding the message to the sender room
            mDbRef.child("Chat").child("Chats").child(senderRoom!!).child("messages").push().setValue(currentMessage).addOnSuccessListener {
                mDbRef.child("Chat").child("Chats").child(receiveRoom!!).child("messages").push().setValue(currentMessage)
            }

            messageBox.setText("")
        }
        backButton.setOnClickListener {
            onBackPressed()
        }

    }
}