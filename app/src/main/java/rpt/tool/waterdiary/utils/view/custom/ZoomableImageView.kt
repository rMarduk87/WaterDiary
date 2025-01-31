package rpt.tool.waterdiary.utils.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.ImageView
import kotlin.math.min

@SuppressLint("AppCompatCustomView")
class ZoomableImageView : ImageView {

    var maxScale: Float = 3f
    var minScale: Float = 1f

    private enum class State {
        INIT, DRAG, ZOOM
    }

    private var state: State? = null
    private var matrix: Matrix? = null
    private val finalTransformation = FloatArray(9)
    private val last = PointF()
    private var currentScale = 1f
    private var viewWidth = 0
    private var viewHeight = 0
    private var afterScaleDrawableWidth = 0f
    private var afterScaleDrawableHeight = 0f

    private var scaleDetector: ScaleGestureDetector? = null

    private var doubleTapDetecture: GestureDetector? = null

    constructor(context: Context) : super(context) {
        setUp(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setUp(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        setUp(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        // Set up drawable at first load
        if (hasDrawable()) {
            resetImage()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector!!.onTouchEvent(event)
        doubleTapDetecture!!.onTouchEvent(event)

        val current = PointF(event.x, event.y)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                last.set(current)
                state = State.DRAG
            }

            MotionEvent.ACTION_MOVE -> if (state == State.DRAG) {
                drag(current)
                last.set(current)
            }

            MotionEvent.ACTION_UP -> state = State.INIT
            MotionEvent.ACTION_POINTER_UP -> state = State.INIT
        }

        imageMatrix = matrix
        invalidate()
        return true
    }

    private fun setUp(context: Context) {
        super.setClickable(false)
        matrix = Matrix()
        state = State.INIT
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
        doubleTapDetecture = GestureDetector(context, GestureListener())
        scaleType = ScaleType.MATRIX
    }

    private fun resetImage() {
        // Scale Image

        val scale = scaleForDrawable
        matrix!!.setScale(scale, scale)

        // Center Image
        val marginY = (viewHeight.toFloat() - (scale * drawable.intrinsicHeight)) / 2
        val marginX = (viewWidth.toFloat() - (scale * drawable.intrinsicWidth)) / 2
        matrix!!.postTranslate(marginX, marginY)

        afterScaleDrawableWidth = viewWidth.toFloat() - 2 * marginX
        afterScaleDrawableHeight = viewHeight.toFloat() - 2 * marginY

        imageMatrix = matrix
    }

    private fun drag(current: PointF) {
        val deltaX = getMoveDraggingDelta(
            current.x - last.x,
            viewWidth.toFloat(),
            afterScaleDrawableWidth * currentScale
        )
        val deltaY = getMoveDraggingDelta(
            current.y - last.y,
            viewHeight.toFloat(),
            afterScaleDrawableHeight * currentScale
        )
        matrix!!.postTranslate(deltaX, deltaY)
        limitDrag()
    }

    private fun scale(focusX: Float, focusY: Float, scaleFactor: Float) {
        var scaleFactor = scaleFactor
        val lastScale = currentScale
        val newScale = lastScale * scaleFactor

        // Calculate next scale with resetting to max or min if required
        if (newScale > maxScale) {
            currentScale = maxScale
            scaleFactor = maxScale / lastScale
        } else if (newScale < minScale) {
            currentScale = minScale
            scaleFactor = minScale / lastScale
        } else {
            currentScale = newScale
        }

        // Do scale
        if (requireCentering()) {
            matrix!!.postScale(
                scaleFactor,
                scaleFactor,
                viewWidth.toFloat() / 2,
                viewHeight.toFloat() / 2
            )
        } else matrix!!.postScale(scaleFactor, scaleFactor, focusX, focusY)

        limitDrag()
    }

    private fun limitDrag() {
        matrix!!.getValues(finalTransformation)
        val finalXTransformation = finalTransformation[Matrix.MTRANS_X]
        val finalYTransformation = finalTransformation[Matrix.MTRANS_Y]

        val deltaX = getScaleDraggingDelta(
            finalXTransformation,
            viewWidth.toFloat(),
            afterScaleDrawableWidth * currentScale
        )
        val deltaY = getScaleDraggingDelta(
            finalYTransformation,
            viewHeight.toFloat(),
            afterScaleDrawableHeight * currentScale
        )

        matrix!!.postTranslate(deltaX, deltaY)
    }

    private fun getScaleDraggingDelta(delta: Float, viewSize: Float, contentSize: Float): Float {
        var minTrans = 0f
        var maxTrans = 0f

        if (contentSize <= viewSize) {
            maxTrans = viewSize - contentSize
        } else {
            minTrans = viewSize - contentSize
        }

        return if (delta < minTrans) minTrans - delta
        else if (delta > maxTrans) (maxTrans - delta)
        else 0f
    }

    // Check if dragging is still possible if so return delta otherwise return 0 (do nothing)
    private fun getMoveDraggingDelta(delta: Float, viewSize: Float, contentSize: Float): Float {
        if (contentSize <= viewSize) {
            return 0f
        }
        return delta
    }

    private val scaleForDrawable: Float
        get() {
            val scaleX =
                viewWidth.toFloat() / drawable.intrinsicWidth.toFloat()
            val scaleY =
                viewHeight.toFloat() / drawable.intrinsicHeight.toFloat()
            return min(scaleX.toDouble(), scaleY.toDouble()).toFloat()
        }

    private fun hasDrawable(): Boolean {
        return drawable != null && drawable.intrinsicWidth != 0 && drawable.intrinsicHeight != 0
    }

    private fun requireCentering(): Boolean {
        return afterScaleDrawableWidth * currentScale <= viewWidth.toFloat() || afterScaleDrawableHeight * currentScale <= viewHeight.toFloat()
    }

    private val isZoom: Boolean
        get() = currentScale != 1f


    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            state = State.ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scale(detector.focusX, detector.focusY, detector.scaleFactor)
            return true
        }
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            if (isZoom) {
                resetImage()
                currentScale = 1f
                state = State.INIT
            } else {
                scale(e.x, e.y, maxScale)
            }
            return true
        }
    }
}