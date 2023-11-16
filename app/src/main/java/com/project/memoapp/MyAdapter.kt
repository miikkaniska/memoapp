import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.project.memoapp.MemoData
import com.project.memoapp.R

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
            val itemString = "Creation Time: ${memo.creationTime}\nName of Memo: ${memo.title}\nMemo data: ${memo.content}"
            textView.text = itemString

            cardView.setOnClickListener {
                // Siirry toiseen fragmenttiin tässä
                val navController = Navigation.findNavController(itemView)
                navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }
    }
}