package rpt.tool.waterdiary.utils.view.custom

import android.text.InputFilter
import android.text.Spanned
import rpt.tool.waterdiary.utils.log.e

class InputFilterWeightRange(private val min: Double, private val max: Double) : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val str = dest.toString() + source.toString()

            val input = (dest.toString() + source.toString()).toDouble()
            if (isInRange(min, max, input, str)) return null
        } catch (nfe: NumberFormatException) {
            nfe.message?.let { e(Throwable(nfe), it) }
        }
        return ""
    }

    private fun isInRange(a: Double, b: Double, c: Double, cc: String): Boolean {
        //return b > a ? c >= a && c <= b : c >= b && c <= a;
        if (cc.length > 5) {
            return false
        }
        if (c > b) {
            return false
        }
        if (c < a) {
            return false
        }

        if (c % 0.5 == 0.0) return true


        return false
    }
}