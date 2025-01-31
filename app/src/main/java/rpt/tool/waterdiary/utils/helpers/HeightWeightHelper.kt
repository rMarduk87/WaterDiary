package rpt.tool.waterdiary.utils.helpers

import rpt.tool.waterdiary.utils.log.e
import java.text.DecimalFormat
import kotlin.math.pow

object HeightWeightHelper {

    private fun format(value: Double): Double {
        try {
            if (value != 0.0) {
                val df = DecimalFormat("###.##")
                return df.format(value).replace(",", ".")
                    .replace("٫", ".").toDouble()
            } else {
                return (-1).toDouble()
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e), it) }
        }

        return (-1).toDouble()
    }


    fun lbToKgConverter(lb: Double): Double {
        return format(lb * 0.453592)
        //0.45359237
    }


    fun kgToLbConverter(kg: Double): Double {
        return format(kg * 2.204624420183777)
        //2.20462262
    }


    fun cmToFeetConverter(cm: Double): Double {
        return format(cm / 30)
    }


    fun feetToCmConverter(feet: Double): Double {
        return format(feet * 30)
    }


    fun getBMIKg(height: Double, weight: Double): Double {
        val meters = height / 100
        return format(weight / meters.pow(2.0))
    }


    fun getBMILb(height: Double, weight: Double): Double {
        val inch = (height * 12).toInt()
        return format((weight * 703) / inch.toDouble().pow(2.0))
    }

    fun getBMIClassification(bmi: Double): String {
        if (bmi <= 0) return "unknown"

        val classification = if (bmi < 18.5) {
            "underweight"
        } else if (bmi < 25) {
            "normal"
        } else if (bmi < 30) {
            "overweight"
        } else {
            "obese"
        }

        return classification
    }

    //=====================================
    fun ozToMlConverter(oz: Float): Double {
        return format(oz * 29.5735)
    }

    fun mlToOzConverter(ml: Float): Double {
        return format(ml * 0.03381405650328842)
    } //0.033814
} //end of class
