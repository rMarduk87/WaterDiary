package rpt.tool.waterdiary.utils.view.custom

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class ClickSpan(private val mListener: OnClickListener?) :
    ClickableSpan() {
    private val isUnderline = false

    var text_color: Int = Color.BLUE

    override fun onClick(widget: View) {
        mListener?.onClick()
    }

    fun interface OnClickListener {
        fun onClick()
    }

    fun setTextColor(color: Int) {
        text_color = color
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = isUnderline
        ds.bgColor = (Color.parseColor("#FAFAFA"))
        ds.color = text_color
    }
}