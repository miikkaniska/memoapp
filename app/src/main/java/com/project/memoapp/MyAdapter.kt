import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.project.memoapp.MemoData
import com.project.memoapp.NewAccountActivity
import com.project.memoapp.NoteActivity
import com.project.memoapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyAdapter(private val memoList: List<MemoData>) : RecyclerView.Adapter<MyAdapter.MemoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MemoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        val memo = memoList[position]
        holder.bind(memo)
    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    inner class MemoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textview_item)
        private val cardView: CardView = itemView.findViewById(R.id.cardview_item)

        fun bind(memo: MemoData) {
            var tempShared : String = ""
            for(i in memo.sharedWith.indices)
            {
                tempShared += memo.sharedWith[i]
                if (i < memo.sharedWith.size - 1 && i != 0) {
                    tempShared += ", "
                }
            }
            val date = Date(memo.creationTime)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(date)

            val itemString = "${memo.title}\nCreated: $formattedDate\nShared with: $tempShared"
            textView.text = itemString

            cardView.setOnClickListener {
                // Siirry toiseen fragmenttiin tässä
                val intent = Intent(itemView.context, NoteActivity::class.java)
                intent.putExtra("memo_id", "memo_id")
                itemView.context.startActivity(intent)
            }
        }
    }
}