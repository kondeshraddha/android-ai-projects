package com.project.aichatapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var adapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    private val api: AnthropicApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.anthropic.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnthropicApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        adapter = ChatAdapter(messages)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            val userText = messageInput.text.toString().trim()
            if (userText.isEmpty()) return@setOnClickListener

            // Show user message
            messages.add(Message(userText, isUser = true))
            adapter.notifyItemInserted(messages.size - 1)
            recyclerView.scrollToPosition(messages.size - 1)
            messageInput.setText("")

            // Call AI
            sendButton.isEnabled = false
            lifecycleScope.launch {
                try {
                    val response = api.sendMessage(
                        ChatRequest(
                            messages = listOf(ApiMessage("user", userText))
                        )
                    )
                    if (response.isSuccessful) {
                        val aiReply = response.body()?.content?.firstOrNull()?.text
                            ?: "No response"
                        messages.add(Message(aiReply, isUser = false))
                        adapter.notifyItemInserted(messages.size - 1)
                        recyclerView.scrollToPosition(messages.size - 1)
                    } else {
                        Toast.makeText(this@MainActivity,
                            "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity,
                        "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    sendButton.isEnabled = true
                }
            }
        }
    }
}