package rpt.tool.waterdiary.utils.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.widget.TextView
import rpt.tool.waterdiary.R
import rpt.tool.waterdiary.utils.log.d
import rpt.tool.waterdiary.utils.view.custom.justify.JustifiedTextView

@SuppressLint("AppCompatCustomView")
class ReadMoreTextView : JustifiedTextView {
    var mContext: Context

    var read_more_text: String? = "Read More"
    var read_less_text: String? = "Read Less"

    var fontName: String? = ""
    var readmore_text_color: Int = Color.BLUE
    var max_line: Int = 1

    var full_desc: String = ""

    constructor(context: Context) : super(context) {
        mContext = context
        setup(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        mContext = context
        setup(attrs)
    }

    private fun setup(attrs: AttributeSet?) {
        if (attrs != null) {
            val a: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView)

            fontName = a.getString(R.styleable.ReadMoreTextView_rmtv_fontname)

            read_more_text = a.getString(R.styleable.ReadMoreTextView_rmtv_read_more_text)

            if (TextUtils.isEmpty(read_more_text)) read_more_text = "Read More"

            read_less_text = a.getString(R.styleable.ReadMoreTextView_rmtv_read_less_text)

            if (TextUtils.isEmpty(read_less_text)) read_more_text = "Read Less"

            readmore_text_color = a.getInteger(
                R.styleable.ReadMoreTextView_rmtv_read_more_text_color,
                readmore_text_color
            )

            max_line = a.getInteger(R.styleable.ReadMoreTextView_rmtv_max_line, max_line)

            a.recycle()
        }

        this._create()
    }

    private fun _create() {
        full_desc = text.toString().trim()

        setTypeFace()
        val des = Html.fromHtml(text.toString().trim()).toString()
        text = Html.fromHtml(des)

        if (!TextUtils.isEmpty(text.toString())) txt_post()
    }

    private fun txt_post() {
        this.post(object : Runnable {
            override fun run() {
                val layout: Layout = layout
                val text: String = text.toString()
                var start = 0
                var end: Int
                val line = arrayOfNulls<String>(lineCount)

                d("@@@@@ : ", "$lineCount @@@ $maxLine")
                println("$lineCount @@@ $maxLine")

                if (lineCount > maxLine) {
                    var str = ""

                    for (i in 0 until lineCount) {
                        if (i > maxLine - 1) break

                        end = layout.getLineEnd(i)
                        line[i] = text.substring(start, end)
                        str += line[i] + " "

                        start = end
                    }

                    if (!TextUtils.isEmpty(str))
                    {
                        str = if (read_more_text!!.contains("...")) {
                            str.substring(0, str.length - 4) + "..."
                        } else {
                            str.substring(
                                0,
                                str.length - (readMoreText!!.length + 4)
                            ) + "... " + read_more_text
                        }
                    }

                    setText(str)

                    read_more_click()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun read_more_click() {
        clickify(this, read_more_text!!) {
            text = full_desc + " " + readLessText
            read_less_click()
        }
    }

    fun read_less_click() {
        clickify(this, read_less_text!!, ClickSpan.OnClickListener {
            text = full_desc
            txt_post()
        })
    }

    var readMoreText: String?
        get() = read_more_text
        set(read_more_text) {
            this.read_more_text = read_more_text
            _create()
        }

    var readLessText: String?
        get() = read_less_text
        set(read_less_text) {
            this.read_less_text = read_less_text
            _create()
        }

    var font: String?
        get() = fontName
        set(fontName) {
            this.fontName = fontName
            _create()
        }

    var myText: String
        get() = full_desc
        set(text) {
            full_desc = text.trim { it <= ' ' }
            setText(text)
            _create()
        }

    var maxLine: Int
        get() = max_line
        set(max_line) {
            this.max_line = max_line
            _create()
        }

    var readMoreTextColor: Int
        get() = readmore_text_color
        set(readmore_text_color) {
            this.readmore_text_color = readmore_text_color
            _create()
        }

    private fun setTypeFace() {
        try {
            if (fontName != null) {
                val myTypeface: Typeface =
                    Typeface.createFromAsset(context.assets, fontName)
                setTypeface(myTypeface)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun clickify(view: TextView, clickableText: String, listener: ClickSpan.OnClickListener?) {
        val text = view.text
        val string = text.toString()
        val span = ClickSpan(listener)
        span.setTextColor(readMoreTextColor)

        val start = string.indexOf(clickableText)
        val end = start + clickableText.length
        if (start == -1) return

        if (text is Spannable) {
            (text as Spannable).setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            val s: SpannableString = SpannableString.valueOf(text)
            s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            view.text = s
        }

        val m: MovementMethod? = view.movementMethod
        if ((m == null) || m !is LinkMovementMethod) {
            view.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}