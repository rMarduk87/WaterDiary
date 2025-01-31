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
import com.bumptech.glide.Glide
import rpt.tool.waterdiary.AppUtils
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.data.model.Container
import rpt.tool.waterdiary.utils.log.e

@SuppressLint("NewApi")
class ContainerAdapterNew(
    var mContext: Context, containerArrayList: ArrayList<Container>,
    private val callBack: CallBack
) :
    RecyclerView.Adapter<ContainerAdapterNew.ViewHolder>() {
    private val containerArrayList: ArrayList<Container> = containerArrayList

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_item_container, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) {
                if (containerArrayList[position].containerValue!!.contains(".")
                ) holder.textView.setText(
                    AppUtils.decimalFormat.format(
                        containerArrayList[position].containerValue!!.toDouble()
                    ) + " " + AppUtils.WATER_UNIT_VALUE
                )
                else holder.textView.text = (containerArrayList[position].containerValue
                        + " " + AppUtils.WATER_UNIT_VALUE)
            } else {
                if (containerArrayList[position].containerValue!!
                        .contains(".")
                ) holder.textView.setText(
                    AppUtils.decimalFormat.format(
                        containerArrayList[position].containerValueOZ!!.toDouble()
                    ) + " " + AppUtils.WATER_UNIT_VALUE
                )
                else holder.textView.text = containerArrayList[position].containerValueOZ +
                        " " + AppUtils.WATER_UNIT_VALUE
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e), it) }
        }

        if (containerArrayList[position].isCustom) Glide.with(mContext)
            .load(R.drawable.ic_custom_ml).into(holder.imageView)
        else Glide.with(mContext).load(getImage(position)).into(holder.imageView)

        holder.item_block.setOnClickListener(View.OnClickListener {
            callBack.onClickSelect(
                containerArrayList[position],
                position
            )
        })

        if (containerArrayList[position].isSelected) {
            holder.img_selected.visibility = View.VISIBLE
        } else {
            holder.img_selected.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return containerArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView =
            itemView.findViewById<TextView>(R.id.container_name)
        var imageView: ImageView =
            itemView.findViewById<ImageView>(R.id.container_img)
        var item_block: LinearLayout = itemView.findViewById<LinearLayout>(R.id.item_block)
        var img_selected: ImageView =
            itemView.findViewById<ImageView>(R.id.img_selected)
    }

    interface CallBack {
        fun onClickSelect(container: Container?, position: Int)
    }

    fun getImage(pos: Int): Int {
        var drawable: Int = R.drawable.ic_custom_ml

        if (AppUtils.WATER_UNIT_VALUE.equals("ml",true)) {
            if (containerArrayList[pos].containerValue!!.toFloat() == 50f) drawable =
                R.drawable.ic_50_ml
            else if (containerArrayList[pos].containerValue!!.toFloat() == 100f) drawable =
                R.drawable.ic_100_ml
            else if (containerArrayList[pos].containerValue!!.toFloat() == 150f) drawable =
                R.drawable.ic_150_ml
            else if (containerArrayList[pos].containerValue!!.toFloat() == 200f) drawable =
                R.drawable.ic_200_ml
            else if (containerArrayList[pos].containerValue!!.toFloat() == 250f) drawable =
                R.drawable.ic_250_ml
            else if (containerArrayList[pos].containerValue!!.toFloat() == 300f) drawable =
                R.drawable.ic_300_ml
            else if (containerArrayList[pos].containerValue!!.toFloat() == 500f) drawable =
                R.drawable.ic_500_ml
            else if (containerArrayList[pos].containerValue!!.toFloat() == 600f) drawable =
                R.drawable.ic_600_ml
            else if (containerArrayList[pos].containerValue!!.toFloat() == 700f) drawable =
                R.drawable.ic_700_ml
            else if (containerArrayList[pos].containerValue!!.toFloat() == 800f) drawable =
                R.drawable.ic_800_ml
            else if (containerArrayList[pos].containerValue!!.toFloat() == 900f) drawable =
                R.drawable.ic_900_ml
            else if (containerArrayList[pos].containerValue!!.toFloat() == 1000f) drawable =
                R.drawable.ic_1000_ml
        } else {
            if (containerArrayList[pos].containerValueOZ!!.toFloat() == 2f) drawable =
                R.drawable.ic_50_ml
            else if (containerArrayList[pos].containerValueOZ!!.toFloat() == 3f) drawable =
                R.drawable.ic_100_ml
            else if (containerArrayList[pos].containerValueOZ!!.toFloat() == 5f) drawable =
                R.drawable.ic_150_ml
            else if (containerArrayList[pos].containerValueOZ!!.toFloat() == 7f) drawable =
                R.drawable.ic_200_ml
            else if (containerArrayList[pos].containerValueOZ!!.toFloat() == 8f) drawable =
                R.drawable.ic_250_ml
            else if (containerArrayList[pos].containerValueOZ!!.toFloat() == 10f) drawable =
                R.drawable.ic_300_ml
            else if (containerArrayList[pos].containerValueOZ!!.toFloat() == 17f) drawable =
                R.drawable.ic_500_ml
            else if (containerArrayList[pos].containerValueOZ!!.toFloat() == 20f) drawable =
                R.drawable.ic_600_ml
            else if (containerArrayList[pos].containerValueOZ!!.toFloat() == 24f) drawable =
                R.drawable.ic_700_ml
            else if (containerArrayList[pos].containerValueOZ!!.toFloat() == 27f) drawable =
                R.drawable.ic_800_ml
            else if (containerArrayList[pos].containerValueOZ!!.toFloat() == 30f) drawable =
                R.drawable.ic_900_ml
            else if (containerArrayList[pos].containerValueOZ!!.toFloat() == 34f) drawable =
                R.drawable.ic_1000_ml
        }

        return drawable
    }
}

