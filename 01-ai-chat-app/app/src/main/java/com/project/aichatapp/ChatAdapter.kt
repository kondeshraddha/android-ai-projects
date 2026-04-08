import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.text

        if (message.isUser) {
            holder.messageText.setBackgroundColor(0xFF6200EE.toInt())
            holder.messageText.setTextColor(0xFFFFFFFF.toInt())
            (holder.itemView as ViewGroup).gravity = Gravity.END
        } else {
            holder.messageText.setBackgroundColor(0xFFE0E0E0.toInt())
            holder.messageText.setTextColor(0xFF000000.toInt())
            (holder.itemView as ViewGroup).gravity = Gravity.START
        }
    }

    override fun getItemCount() = messages.size
}