package rpt.tool.waterdiary.utils.view.custom


import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import rpt.tool.waterdiary.R

class FloatingHintEditTextLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    FrameLayout(context, attrs, defStyle) {
    private var mEditText: EditText? = null
    var label: TextView? = null

    private val inAnimation: Animation
    private val outAnimation: Animation
    private var isAnimating = false

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        var params = params
        if (child is EditText) {
            require(mEditText == null) { "We already have an EditText, can only have one" }

            val lp: LayoutParams = LayoutParams(params)
            lp.gravity = Gravity.BOTTOM
            lp.topMargin = label!!.textSize.toInt()
            params = lp

            editText = child
        }

        super.addView(child, index, params)
    }

    var editText: EditText?
      
        get() = mEditText
        private set(editText) {
            mEditText = editText
            mEditText!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if (TextUtils.isEmpty(s)) {
                        if (label!!.visibility == View.VISIBLE || isAnimating) {
                            hideLabel()
                        }
                    } else {
                        if ((label!!.visibility != View.VISIBLE) and !isAnimating) {
                            showLabel()
                        }
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }
            })

            mEditText!!.setOnFocusChangeListener { view, focused -> label!!.isActivated = focused }

            label!!.text = mEditText!!.hint
        }
    
    private fun showLabel() {
        label!!.startAnimation(inAnimation)
    }
    
    private fun hideLabel() {
        label!!.startAnimation(outAnimation)
    }
    
    private fun dipsToPix(dps: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dps,
            resources.displayMetrics
        ).toInt()
    }
    
    private val showLabelAnimationListener: Animation.AnimationListener =
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                isAnimating = true
            }

            override fun onAnimationEnd(animation: Animation) {
                label!!.visibility = View.VISIBLE
                isAnimating = false
            }

            override fun onAnimationRepeat(animation: Animation) {}
        }
    
    private val hideLabelAnimationListener: Animation.AnimationListener =
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                isAnimating = true
            }

            override fun onAnimationEnd(animation: Animation) {
                label!!.visibility = View.GONE
                isAnimating = false
            }

            override fun onAnimationRepeat(animation: Animation) {}
        }

    init {
        val a: TypedArray = context
            .obtainStyledAttributes(attrs, R.styleable.FloatingHintEditTextLayout)

        inAnimation = AnimationUtils.loadAnimation(
            getContext(),
            a.getResourceId(
                R.styleable.FloatingHintEditTextLayout_floatingHintInAnimation,
                R.anim.slide_in_top
            )
        )
        inAnimation.setAnimationListener(showLabelAnimationListener)

        outAnimation = AnimationUtils.loadAnimation(
            getContext(),
            a.getResourceId(
                R.styleable.FloatingHintEditTextLayout_floatingHintOutAnimation,
                R.anim.slide_out_bottom
            )
        )
        outAnimation.setAnimationListener(hideLabelAnimationListener)

        val sidePadding: Int = a.getDimensionPixelSize(
            R.styleable.FloatingHintEditTextLayout_floatingHintSidePadding,
            dipsToPix(DEFAULT_PADDING_LEFT_RIGHT_DP)
        )

        label = TextView(context)
        label!!.setPadding(sidePadding, 0, sidePadding, 0)
        label!!.visibility = View.INVISIBLE
        label!!.setTextAppearance(
            context,
            a.getResourceId(
                R.styleable.FloatingHintEditTextLayout_floatingHintTextAppearance,
                android.R.style.TextAppearance_Small
            )
        )

        addView(label, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        a.recycle()
    }

    companion object {
        private const val DEFAULT_PADDING_LEFT_RIGHT_DP = 0f
    }
}