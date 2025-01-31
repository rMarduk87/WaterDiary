package rpt.tool.waterdiary.utils.view.custom

import android.text.InputFilter
import android.text.Spanned
import rpt.tool.waterdiary.utils.log.e

class DigitsInputFilter(
    private val mMaxIntegerDigitsLength: Int,
    private val mMaxDigitsAfterLength: Int,
    private val mMax: Double
) :
    InputFilter {
    private val DOT = "."


    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val allText = getAllText(source, dest, dstart)
        val onlyDigitsText = getOnlyDigitsPart(allText)

        if (allText.isEmpty()) {
            return null
        } else {
            val enteredValue: Double
            try {
                enteredValue = onlyDigitsText.toDouble()
            } catch (e: NumberFormatException) {
                e.message?.let { e(Throwable(e), it) }
                return ""
            }
            return checkMaxValueRule(enteredValue, onlyDigitsText)
        }
    }


    private fun checkMaxValueRule(enteredValue: Double, onlyDigitsText: String): CharSequence? {
        return if (enteredValue > mMax) {
            ""
        } else {
            handleInputRules(onlyDigitsText)
        }
    }

    private fun handleInputRules(onlyDigitsText: String): CharSequence? {
        return if (isDecimalDigit(onlyDigitsText)) {
            checkRuleForDecimalDigits(onlyDigitsText)
        } else {
            checkRuleForIntegerDigits(onlyDigitsText.length)
        }
    }

    private fun isDecimalDigit(onlyDigitsText: String): Boolean {
        return onlyDigitsText.contains(DOT)
    }

    private fun checkRuleForDecimalDigits(onlyDigitsPart: String): CharSequence? {
        val afterDotPart =
            onlyDigitsPart.substring(onlyDigitsPart.indexOf(DOT), onlyDigitsPart.length - 1)
        if (afterDotPart.length > mMaxDigitsAfterLength) {
            return ""
        }
        return null
    }

    private fun checkRuleForIntegerDigits(allTextLength: Int): CharSequence? {
        if (allTextLength > mMaxIntegerDigitsLength) {
            return ""
        }
        return null
    }

    private fun getOnlyDigitsPart(text: String): String {
        return text.replace("[^0-9?!]".toRegex(), "")
    }

    private fun getAllText(source: CharSequence, dest: Spanned, dstart: Int): String {
        var allText = ""
        if (dest.toString().isNotEmpty()) {
            allText = if (source.toString().isEmpty()) {
                deleteCharAtIndex(dest, dstart)
            } else {
                StringBuilder(dest).insert(dstart, source).toString()
            }
        }
        return allText
    }

    private fun deleteCharAtIndex(dest: Spanned, dstart: Int): String {
        val builder = StringBuilder(dest)
        builder.deleteCharAt(dstart)
        return builder.toString()
    }
}