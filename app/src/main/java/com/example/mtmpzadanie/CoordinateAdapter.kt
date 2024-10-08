import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mtmpzadanie.CoordinateData
import com.example.mtmpzadanie.R

class CoordinateAdapter(private val coordinateList: List<CoordinateData>) :
    RecyclerView.Adapter<CoordinateAdapter.CoordinateViewHolder>() {

    class CoordinateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewXValue: TextView = view.findViewById(R.id.textViewXValue)
        val textViewYValue: TextView = view.findViewById(R.id.textViewYValue)
        val textViewTValue: TextView = view.findViewById(R.id.textViewTValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoordinateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coordinate, parent, false)
        return CoordinateViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoordinateViewHolder, position: Int) {
        val coordinate = coordinateList[position]
        holder.textViewXValue.text = String.format("%.3f", coordinate.x)
        holder.textViewYValue.text = String.format("%.3f", coordinate.y)
        holder.textViewTValue.text = String.format("%.3f", coordinate.t)
    }

    override fun getItemCount(): Int = coordinateList.size
}
