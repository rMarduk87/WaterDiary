package rpt.tool.waterdiary.utils.view.custom

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.graphics.Typeface
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.utils.log.e


@SuppressLint("AppCompatCustomView")
class MyButton : android.widget.Button {
    var fontName: String? = null


    constructor(
        context: android.content.Context?,
        attrs: android.util.AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    constructor(
        context: android.content.Context?,
        attrs: android.util.AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init(attrs)
    }

    constructor(context: android.content.Context?, attrs: android.util.AttributeSet?) : super(
        context,
        attrs
    ) {
        init(attrs)
    }

    constructor(context: android.content.Context?) : super(context) {
        init(null)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: android.util.AttributeSet?) {
        if (attrs != null) {
            val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView)
            fontName = a.getString(R.styleable.MyTextView_fontname)

            setTypeFace()

            a.recycle()
        }
    }

    fun valorizetFontName(fontName: String?) {
        this.fontName = fontName
        setTypeFace()
    }

    private fun setTypeFace() {
        try {
            if (fontName != null) {
                val myTypeface: Typeface = Typeface.createFromAsset(context.assets, fontName)
                typeface = myTypeface
            }
        } catch (e: java.lang.Exception) {
            e.message?.let { e(Throwable(e), it) }
            e.printStackTrace()
        }
    }
}
