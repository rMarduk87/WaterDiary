package rpt.tool.waterdiary.utils.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.data.model.IntervalModel

@SuppressLint("NewApi")
class IntervalAdapter(
    var mContext: Context, intervals: List<IntervalModel>,
    var callBack: CallBack
) :
    RecyclerView.Adapter<IntervalAdapter.ViewHolder>() {
    private val intervals: List<IntervalModel> = intervals

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_item_sound, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lbl_sound_name.text = intervals[position].name

        if (intervals[position].isSelected) {
            holder.img_selected.visibility = View.VISIBLE
            holder.item_block.background
                .setTint(mContext.resources.getColor(R.color.colorPrimary))
        } else {
            holder.img_selected.visibility = View.INVISIBLE
            holder.item_block.background.setTint(mContext.resources.getColor(R.color.white))
        }

        holder.item_block.setOnClickListener {
            callBack.onClickSelect(
                intervals[position],
                position
            )
        }
    }


    override fun getItemCount(): Int {
        return intervals.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img_selected: ImageView =
            itemView.findViewById<ImageView>(R.id.img_selected)
        var item_block: LinearLayout = itemView.findViewById<LinearLayout>(R.id.item_block)
        var lbl_sound_name: TextView =
            itemView.findViewById<TextView>(R.id.lbl_sound_name)
    }

    interface CallBack {
        fun onClickSelect(time: IntervalModel?, position: Int)
    }
}

