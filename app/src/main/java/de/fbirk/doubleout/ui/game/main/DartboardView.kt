package de.fbirk.doubleout.ui.game.main

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import de.fbirk.doubleout.R
import de.fbirk.doubleout.functions.DrawArcSegment


class DartboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textArray = arrayListOf<String>(
        "6",
        "10",
        "15",
        "2",
        "17",
        "3",
        "19",
        "7",
        "16",
        "8",
        "11",
        "14",
        "9",
        "12",
        "5",
        "20",
        "1",
        "18",
        "4",
        "13",
        "6"
    )
    private val quarters = arrayListOf<Path>()
    private var visibleQuarter: Path? = null
    private val arcSegmentFunctions = DrawArcSegment()
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val backgroundColor =
        ResourcesCompat.getColor(resources, R.color.design_default_color_background, null)
    private val blackColor = ResourcesCompat.getColor(resources, R.color.black, null)
    private val greenColor = ResourcesCompat.getColor(resources, R.color.everglade_green, null)
    private val redColor = ResourcesCompat.getColor(resources, R.color.mule_fawn_red, null)
    private val khakiColor = ResourcesCompat.getColor(resources, R.color.indian_khaki, null)
    private val whiteColor = ResourcesCompat.getColor(resources, R.color.white, null)
    private val highlightOne =
        ResourcesCompat.getColor(resources, R.color.design_default_color_background, null)
    private val highlightTwo =
        ResourcesCompat.getColor(resources, R.color.design_default_color_background, null)

    private val highlight = Paint()

    private lateinit var myCanvas: Canvas


    init {
        isClickable = true
        highlight.color = highlightOne
        highlight.alpha = 100
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)

        myCanvas = canvas!!

        val blackField = Paint()
        blackField.color = blackColor
        val greenFieldColor = Paint()
        greenFieldColor.color = greenColor
        val redField = Paint()
        redField.color = redColor
        val khakiField = Paint()
        khakiField.color = khakiColor
        val white = Paint()
        white.color = whiteColor
        white.textSize = 40F;

        val cx = (width / 2).toFloat()
        val cy = (height / 2).toFloat()
        val length = (Math.min(width, height) / 2).toDouble()

        // each segment is 18° wide and middle centered, so we start with a offset of 9°
        var startingAngle: Double = -9.0
        // create all 20 segments
        for (i: Int in 0..19) {
            var normalColor: Paint  // color for single value fields
            var specialColor: Paint // color for double/triple fields

            // toggle color for every other segment
            if (i % 2 == 0) {
                normalColor = khakiField
                specialColor = greenFieldColor
            } else {
                normalColor = blackField
                specialColor = redField
            }

            // draw all fields in one segment (inner single, triple, outer single, double and black board fields)
            for (j: Int in 0..5) {
                val rInn = getInnerRadius(length, j)
                val rOut = getROut(length, j)
                // toggle between normal and special color every other field - the outer field is black
                val color: Paint = when {
                    j == 5 -> blackField
                    j % 2 == 0 -> specialColor
                    else -> normalColor
                }

                // draw a single arc segment with the given width of 18°
                arcSegmentFunctions.drawArcSegment(
                    canvas!!,
                    cx,
                    cy,
                    rInn.toFloat(),
                    rOut.toFloat(),
                    startingAngle,
                    18.0F,
                    color
                )
                if (j == 5 && i <= textArray.size) {
                    arcSegmentFunctions.drawText(
                        canvas,
                        cx,
                        cy,
                        rInn.toFloat(),
                        rOut.toFloat(),
                        startingAngle,
                        18.0F,
                        textArray[i],
                        white
                    )
                }
            }

            startingAngle += 18.0
        }

        // inner bulls eye
        arcSegmentFunctions.drawArcSegment(
            canvas!!,
            cx,
            cy,
            0.0F,
            (length * (12.7 / 451.0)).toFloat(),
            0.0,
            360.0F,
            redField
        )

        // outer bulls eye
        arcSegmentFunctions.drawArcSegment(
            canvas,
            cx,
            cy,
            (length * (12.7 / 451.0)).toFloat(),
            ((length * (12.7 / 451.0)) + (length * (19.3 / 451.0))).toFloat(),
            0.0,
            360.0F,
            greenFieldColor
        )

        drawOverlays(canvas, cx, cy, length)

        if (visibleQuarter != null) {
            canvas.drawPath(visibleQuarter!!, highlight)
        }
    }

    private fun drawOverlays(canvas: Canvas?, cx: Float, cy: Float, length: Double) {
        val transparentColor = Paint()
        transparentColor.color = highlightOne
        transparentColor.alpha = 0

        val white = Paint()
        white.color = whiteColor
        val rInn = length * 0.18
        var startingAngle: Double = -90.0

        for (i: Int in 0..3) {

            quarters.add(
                arcSegmentFunctions.drawArcSegment(
                    canvas!!,
                    cx,
                    cy,
                    rInn.toFloat(),
                    length.toFloat(),
                    startingAngle,
                    90.0F,
                    transparentColor,
                    white
                )
            )
            startingAngle += 90.0
        }

        // outer bulls eye
        quarters.add(
            arcSegmentFunctions.drawArcSegment(
                canvas!!,
                cx,
                cy,
                0.0F,
                rInn.toFloat(),
                0.0,
                360.0F,
                transparentColor,
                white
            )
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled) {
            return false
        }

        val x = event?.x
        val y = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val quarter = checkForMatchingQuarter(x!!, y!!)
                if (quarter != null) {
                    visibleQuarter = quarter
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                visibleQuarter = null
                invalidate()
            }
        }

        return true
    }

    private fun checkForMatchingQuarter(x: Float, y: Float): Path? {
        quarters.forEachIndexed { index, path ->
            val boundingRegion = RectF()
            quarters[index].computeBounds(boundingRegion, true)
            if (boundingRegion.contains(x, y)) {
                return quarters[index]
            }
        }
        return null
    }

    // get outer radius of a arc segment depending on the segments position and the overall width of the dart board
    private fun getROut(length: Double, element: Int): Double {
        val maxWidth = 451.0
        return when (element) {
            1 -> (length * (12.7 / maxWidth)) + (length * (19.3 / maxWidth)) + (length * (166.0 / maxWidth))
            2 -> getROut(length, 1) + (length * (16.0 / maxWidth))
            3 -> getROut(length, 2) + (length * (110.0 / maxWidth))
            4 -> getROut(length, 3) + (length * (16.0 / maxWidth))
            else -> getROut(length, 4) + (length * (111.0 / maxWidth))
        }
    }

    private fun getInnerRadius(length: Double, element: Int): Double {
        val maxWidth = 451.0
        return when (element) {
            1 -> (length * (12.7 / maxWidth)) + (length * (19.3 / maxWidth))
            2 -> getInnerRadius(length, 1) + (length * (166.0 / maxWidth))
            3 -> getInnerRadius(length, 2) + (length * (16.0 / maxWidth))
            4 -> getInnerRadius(length, 3) + (length * (110.0 / maxWidth))
            else -> getInnerRadius(length, 4) + (length * (16.0 / maxWidth))
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
}