package rpt.tool.waterdiary.utils.view.custom

import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import rpt.tool.waterdiary.utils.log.d
import rpt.tool.waterdiary.utils.log.e

class InputFilterRange(private val min: Double, var elements: List<Double>) :
    InputFilter {
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
            d("CharSequence", " -> " + str.length)

            /*if(str.length()>=5)
                return null;*/
            val input = (dest.toString() + source.toString()).toDouble()
            if (isInRange(min, elements, input, str)) return null
        } catch (nfe: NumberFormatException) {
            nfe.message?.let { e(Throwable(nfe), it) }
        }
        return ""
    }

    private fun isInRange(a: Double, b: List<Double>, c: Double, cc: String): Boolean {
        //return b > a ? c >= a && c <= b : c >= b && c <= a;
        if (cc.length > 4) {
            return false
        }

        for (k in b.indices) {

            if (b[k] == c)  //if(tb.equalsIgnoreCase(tc))
                return true
        }
        return false
    }
}