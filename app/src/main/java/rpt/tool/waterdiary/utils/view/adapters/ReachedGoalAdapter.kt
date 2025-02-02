package rpt.tool.waterdiary.utils.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rpt.tool.waterdiary.AppUtils
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.data.model.History
import rpt.tool.waterdiary.data.model.ReachedGoal

@SuppressLint("NewApi")
class ReachedGoalAdapter(
    var mContext: Context, reachedGoalArrayList: ArrayList<ReachedGoal>,
    var callBack: CallBack
) :
    RecyclerView.Adapter<ReachedGoalAdapter.ViewHolder>() {
    private val reachedGoalArrayList: ArrayList<ReachedGoal> = reachedGoalArrayList

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_item_reached, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.lbl_date.text = reachedGoalArrayList[position].date

        if (AppUtils.WATER_UNIT_VALUE.equals("ml",true))
            holder.lbl_quantity.text = reachedGoalArrayList[position].containerValue +
                    " " + AppUtils.WATER_UNIT_VALUE
        else holder.lbl_quantity.text = reachedGoalArrayList[position].containerValueOZ +
                " " + AppUtils.WATER_UNIT_VALUE
    }

    override fun getItemCount(): Int {
        return reachedGoalArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lbl_date: TextView =
            itemView.findViewById<TextView>(R.id.lbl_date)
        var lbl_quantity: TextView =
            itemView.findViewById<TextView>(R.id.quantity_name)
    }

    interface CallBack {
        fun onClickSelect(reachedGoal: ReachedGoal, position: Int)
    }
}