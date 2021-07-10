package de.fbirk.doubleout.ui.game.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
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
import kotlin.math.sin
import kotlin.properties.Delegates


class DartboardSegmentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var touchedSegment: Pair<Path, Field>
    private var segments = arrayListOf<Pair<Path, Field>>()
    private val arcSegmentFunctions = DrawArcSegment()
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val backgroundColor =
        ResourcesCompat.getColor(resources, R.color.design_default_color_background, null)

    private var selectedSegment by Delegates.notNull<Int>()

    private val trPoints = arrayListOf(20, 1, 18, 4, 13, 6)
    private val brPoints = arrayListOf(6, 10, 15, 2, 17, 3)
    private val blPoints = arrayListOf(3, 19, 7, 16, 8, 11)
    private val tlPoints = arrayListOf(11, 14, 9, 12, 5, 20)
    private val middlePoints = arrayListOf(25, 50)

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.DartboardSegmentView) {
            selectedSegment = getInt(R.styleable.DartboardSegmentView_selected_segment, 0)
        }
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
        val blackField = Paint()
        blackField.color = ResourcesCompat.getColor(resources, R.color.black, null)
        val greenFieldColor = Paint()
        greenFieldColor.color = ResourcesCompat.getColor(resources, R.color.everglade_green, null)
        val redField = Paint()
        redField.color = ResourcesCompat.getColor(resources, R.color.mule_fawn_red, null)
        val khakiField = Paint()
        khakiField.color = ResourcesCompat.getColor(resources, R.color.indian_khaki, null)

        val radius = (Math.min(width, height) * 0.6)
        val cx = getCXValue(radius)
        val cy = getCYValue(radius)

        if (selectedSegment < 4) {
            var startingAngle: Double = getStartingAngleFromSelectedSegment()
            for (i: Int in 0..5) {
                var normalColor: Paint
                var specialColor: Paint

                if (i % 2 == selectedSegment % 2) {
                    normalColor = khakiField
                    specialColor = greenFieldColor
                } else {
                    normalColor = blackField
                    specialColor = redField
                }

                for (j: Int in 0..5) {
                    val rInn = getInnerRadius(radius, j)
                    val rOut = getROut(radius, j)
                    val color: Paint = when {
                        j == 5 -> blackField
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

                    if (j < 4) {
                        segments.add(Pair(drawnSegment, getCurrentField(i, j)))
                    }
                }
                startingAngle += 18.0
            }
        } else {
            drawMiddleCircle(canvas, redField, greenFieldColor)
        }
    }

    private fun getCurrentField(i: Int, j: Int): Field {
        var factor = 0
        var value = 0
        var pointsList = when (selectedSegment) {
            0 -> brPoints
            1 -> blPoints
            2 -> tlPoints
            3 -> trPoints
            else -> middlePoints
        }

        value = pointsList[i]

        if (j == 0 || j == 2) {
            factor = 1
        } else if (j == 1) {
            factor = 3
        } else {
            factor = 2
        }
        return Field(value, factor)
    }

    private fun drawMiddleCircle(canvas: Canvas?, redField: Paint, greenFieldColor: Paint) {
        var cx = (width / 2).toFloat()
        var cy = (height / 2).toFloat()
        var length = (Math.min(width, height) * 3 / 4).toDouble()

        // inner bulls eye
        var innerSegment = arcSegmentFunctions.drawArcSegment(
            canvas!!,
            cx,
            cy,
            0.0F,
            (length * (12.7 / 451.0)).toFloat(),
            0.0,
            360.0F,
            redField
        )

        segments.add(Pair(innerSegment, Field(25, 1)))

        // outer bulls eye
        var outerSegment = arcSegmentFunctions.drawArcSegment(
            canvas,
            cx,
            cy,
            (length * (12.7 / 451.0)).toFloat(),
            ((length * (12.7 / 451.0)) + (length * (19.3 / 451.0))).toFloat(),
            0.0,
            360.0F,
            greenFieldColor
        )
        segments.add(Pair(outerSegment, Field(25, 2)))
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        performClick()
        if (!isEnabled) {
            return false
        }

        val x = event?.x
        val y = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                var result = findMatchingSegmentToPoint(segments, x!!, y!!)
                if (result != null) {
                    clickDartboardSegmentListener(result)
                }
                invalidate()
            }
        }

        return true
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun findMatchingSegmentToPoint(
        segments: ArrayList<Pair<Path, Field>>,
        x: Float,
        y: Float
    ): Field? {

        var touchCoordinatePath: Path = Path()
        touchCoordinatePath.moveTo(x, y)
        touchCoordinatePath.lineTo(x, 0.0F)

        for (element in segments) {
            var path = element.first
            if (path.op(touchCoordinatePath, path, Path.Op.INTERSECT)) {
                return  element.second
            }
        }
        return null
    }

    private fun getCXValue(radius: Double): Float {
        return if (selectedSegment == 0 || selectedSegment == 4) {
            ((width / 2) - (width / 4) - getChordLength(radius)).toFloat()
        } else {
            ((width / 2) + (width / 4) + getChordLength(radius)).toFloat()
        }
    }

    private fun getCYValue(radius: Double): Float {
        return if (selectedSegment < 2) {
            ((height / 2) - (height / 4) - getChordLength(radius)).toFloat()
        } else {
            ((height / 2) + (height / 4) + getChordLength(radius)).toFloat()
        }
    }

    private fun getChordLength(radius: Double): Double {
        // berechnen der Sehen-Länge (Strecke von Anfang zum Ende eines Kreisbogens)
        // c = 2 * R * sin(α / 2)
        // 9° for alpha in radians, for the part greater than 90°
        return 2 * radius * sin(0.157 / 2)
    }

    private fun getStartingAngleFromSelectedSegment(): Double {
        return when (selectedSegment) {
            0 -> -9.0
            1 -> 81.0
            2 -> 171.0
            else -> 261.0
        }
    }

    private fun getROut(length: Double, element: Int): Double {
        return when (element) {
            1 -> (length * 0.037) + (length * 0.057) + (length * 0.488)
            2 -> getROut(length, 1) + (length * 0.047)
            3 -> getROut(length, 2) + (length * 0.324)
            4 -> getROut(length, 3) + (length * 0.047)
            else -> getROut(length, 4) + (length * 0.326)
        }
    }

    private fun getInnerRadius(length: Double, element: Int): Double {
        return when (element) {
            1 -> (length * 0.037) + (length * 0.057)
            2 -> getInnerRadius(length, 1) + (length * 0.488)
            3 -> getInnerRadius(length, 2) + (length * 0.047)
            4 -> getInnerRadius(length, 3) + (length * 0.324)
            else -> getInnerRadius(length, 4) + (length * 0.047)
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