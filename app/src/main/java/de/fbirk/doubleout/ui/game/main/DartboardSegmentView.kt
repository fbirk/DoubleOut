package de.fbirk.doubleout.ui.game.main

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import de.fbirk.doubleout.R
import de.fbirk.doubleout.functions.DrawArcSegment
import de.fbirk.doubleout.model.Field
import kotlin.math.min
import kotlin.math.sin
import kotlin.properties.Delegates


class DartboardSegmentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private var selectedSegment by Delegates.notNull<Int>()
    private var highlightedField: Path? = null
    private var segments = arrayListOf<Pair<Path, Field>>()
    private val arcSegmentFunctions = DrawArcSegment()

    private var selectedSegmentPoints: ArrayList<Int>

    private val blackFieldPaint = Paint()
    private val greenFieldPaint = Paint()
    private val redFieldPaint = Paint()
    private val khakiFieldPaint = Paint()
    private val highlightPaint = Paint()
    private val whitePaint = Paint()
    private val backgroundColor: Int

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.DartboardSegmentView) {
            selectedSegment = getInt(R.styleable.DartboardSegmentView_selected_segment, 0)
        }

        selectedSegmentPoints = getCurrentPointsArray()

        blackFieldPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        greenFieldPaint.color = ResourcesCompat.getColor(resources, R.color.everglade_green, null)
        redFieldPaint.color = ResourcesCompat.getColor(resources, R.color.mule_fawn_red, null)
        khakiFieldPaint.color = ResourcesCompat.getColor(resources, R.color.indian_khaki, null)
        highlightPaint.color = ResourcesCompat.getColor(resources, R.color.highlight1, null)
        whitePaint.color = ResourcesCompat.getColor(resources, R.color.white, null)
        whitePaint.textSize = 40F;
        backgroundColor =
            ResourcesCompat.getColor(resources, R.color.design_default_color_background, null)
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)

        val radius = (min(width, height) * 0.6)
        val cx = getCXValue(radius)
        val cy = getCYValue(radius)

        if (selectedSegment < 4) {
            var startingAngle: Double = getStartingAngleFromSelectedSegment()
            for (i: Int in 0..5) {
                var normalColor: Paint
                var specialColor: Paint

                // bottom right and top left quarter start with a khaki field
                // BL and TR quarters start with a black field
                // alternate beginning color on selected dartboard segment
                if (i % 2 == selectedSegment % 2) {
                    normalColor = khakiFieldPaint
                    specialColor = greenFieldPaint
                } else {
                    normalColor = blackFieldPaint
                    specialColor = redFieldPaint
                }

                for (j: Int in 1..5) {
                    val rInn = getInnerRadius(radius, j)
                    val rOut = getOuterRadius(radius, j)
                    val color: Paint = when {
                        j == 5 -> blackFieldPaint
                        j % 2 == 0 -> specialColor
                        else -> normalColor
                    }

                    val drawnSegment = arcSegmentFunctions.drawArcSegment(
                        canvas!!,
                        cx,
                        cy,
                        rInn.toFloat(),
                        rOut.toFloat(),
                        startingAngle,
                        18.0F,
                        color
                    )

                    if (j == 5 && i <= 6) {
                        arcSegmentFunctions.drawText(
                            canvas,
                            cx,
                            cy,
                            rInn.toFloat(),
                            rOut.toFloat(),
                            startingAngle,
                            18.0F,
                            selectedSegmentPoints[i].toString(),
                            whitePaint
                        )
                    }

                    // add all clickable fields to a list to check for touch events, ignore the black outer ring
                    if (j < 5) {
                        segments.add(Pair(drawnSegment, getCurrentField(i, j)))
                    }
                }
                startingAngle += 18.0
            }
        } else {
            drawMiddleCircle(canvas, redFieldPaint, greenFieldPaint)
        }

        // highlight a click
        if (highlightedField != null) {
            canvas!!.drawPath(highlightedField!!, highlightPaint)
        }
    }

    /**
     * Return a sequence of six dartboard field values for a selected quarter or the middle circle.
     * The sequence is ordered clockwise.
     */
    private fun getCurrentPointsArray(): ArrayList<Int> {
        return when (selectedSegment) {
            0 -> arrayListOf(6, 10, 15, 2, 17, 3)
            1 -> arrayListOf(3, 19, 7, 16, 8, 11)
            2 -> arrayListOf(11, 14, 9, 12, 5, 20)
            3 -> arrayListOf(20, 1, 18, 4, 13, 6)
            else -> arrayListOf(25, 50)
        }
    }

    /**
     * Depending on the selected quarter decide the starting angle of the shown quarter segment.
     * The circle begins with 0° for the 3 o'clock value instead of the top (or 12 o'clock value)
     */
    private fun getStartingAngleFromSelectedSegment(): Double {
        return when (selectedSegment) {
            0 -> -9.0       // bottom right
            1 -> 81.0       // bottom left
            2 -> 171.0      // top left
            else -> 261.0   // top right
        }
    }

    /**
     * Get the corresponding Field-object value for a touched field element.
     */
    private fun getCurrentField(i: Int, j: Int): Field {
        val value: Int = selectedSegmentPoints[i]

        val factor: Int = if (j == 1 || j == 3) {
            1
        } else if (j == 2) {
            3
        } else {
            2
        }
        return Field(value, factor, j)
    }

    /**
     * Draws the inner circle with inner and outer bulls eye.
     */
    private fun drawMiddleCircle(canvas: Canvas?, redField: Paint, greenFieldColor: Paint) {
        val cx = (width / 2).toFloat()
        val cy = (height / 2).toFloat()
        val length = (min(width, height) * 3 / 4).toDouble()

        // inner bulls eye
        val innerSegment = arcSegmentFunctions.drawArcSegment(
            canvas!!,
            cx,
            cy,
            0.0F,
            (length * (50.8 / 451.0)).toFloat(),
            0.0,
            360.0F,
            redField
        )

        segments.add(Pair(innerSegment, Field(25, 1)))

        // outer bulls eye
        val outerSegment = arcSegmentFunctions.drawArcSegment(
            canvas,
            cx,
            cy,
            (length * (50.8 / 451.0)).toFloat(),
            ((length * (50.8 / 451.0)) + (length * (77.2 / 451.0))).toFloat(),
            0.0,
            360.0F,
            greenFieldColor
        )
        segments.add(Pair(outerSegment, Field(25, 2)))
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    /**
     * On a touch event, check which dartboard field was touched.
     * Passes the selected Field to the 'clickDartboardSegmentListener'
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        performClick()
        if (!isEnabled) return false

        val x = event?.x
        val y = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val result = findMatchingSegmentToPoint(segments, x!!, y!!)
                if (result != null) {
                    clickDartboardSegmentListener(result)
                }
                segments.clear()
                invalidate()
            }
        }
        return true
    }

    /**
     * Generate a rectangle with edge length 1 of the touch input coordinate.
     * Check if the resulting rectangle intersects with a bounding box rectangle of
     * any of the dartboard segments.
     *
     * @return a dartboard Field object or null
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun findMatchingSegmentToPoint(
        segments: ArrayList<Pair<Path, Field>>,
        x: Float,
        y: Float
    ): Field? {
        // create a rectangle with edge length of 1 for the touch input
        val touchPoint = RectF(x, y, x + 1, y + 1)
        val touchPointPath = Path()
        touchPointPath.addRect(touchPoint, Path.Direction.CW)

        for (element in segments) {
            val path = element.first
            val copyPath = Path(path)
            if (copyPath.op(touchPointPath, Path.Op.INTERSECT)) {
                val bounds = RectF()
                copyPath.computeBounds(bounds, true)
                // check if touch rectangle intersects with current field element
                // if so, the touch coordinate lies within the field
                if (bounds.left != 0.0F && bounds.top != 0.0F && bounds.right != 0.0F && bounds.bottom != 0.0F) {
                    highlightedField = path
                    return element.second
                }
            }
        }
        touchPointPath.reset()
        return null
    }

    private fun getCXValue(radius: Double): Float {
        return if (selectedSegment == 0 || selectedSegment == 3) {
            ((width / 2) - (width / 4) - getChordLength(radius)).toFloat()
        } else {
            ((width / 2) + (width / 4) + getChordLength(radius)).toFloat()
        }
    }

    private fun getCYValue(radius: Double): Float {
        return if (selectedSegment < 2) {
            ((height / 2) - (height / 4) + getChordLength(radius)).toFloat()
        } else {
            ((height / 2) + (height / 4) - getChordLength(radius)).toFloat()
        }
    }

    private fun getChordLength(radius: Double): Double {
        // c = 2 * R * sin(α / 2)
        // 9° for alpha in radians, for the part greater than 90°
        return 2 * radius * sin(0.157 / 2)
    }

    private fun getOuterRadius(length: Double, element: Int): Double {
        return when (element) {
            1 -> (length * 0.037) + (length * 0.057) + (length * 0.488)
            2 -> getOuterRadius(length, 1) + (length * 0.100)
            3 -> getOuterRadius(length, 2) + (length * 0.324)
            4 -> getOuterRadius(length, 3) + (length * 0.100)
            else -> getOuterRadius(length, 4) + (length * 0.232)
        }
    }

    private fun getInnerRadius(length: Double, element: Int): Double {
        return when (element) {
            1 -> (length * 0.037) + (length * 0.057)
            2 -> getInnerRadius(length, 1) + (length * 0.488)
            3 -> getInnerRadius(length, 2) + (length * 0.100)
            4 -> getInnerRadius(length, 3) + (length * 0.324)
            else -> getInnerRadius(length, 4) + (length * 0.100)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Prevent memory leak
        if (::extraBitmap.isInitialized) extraBitmap.recycle()

        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    lateinit var onDartboardSegmentClicked: (Field) -> Unit

    private fun clickDartboardSegmentListener(field: Field) {
        onDartboardSegmentClicked(field)
    }
}