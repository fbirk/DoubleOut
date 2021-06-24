package de.fbirk.doubleout.ui.game.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import de.fbirk.doubleout.R
import de.fbirk.doubleout.functions.DrawArcSegment


class DartboardSegmentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val arcSegmentFunctions = DrawArcSegment()
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val backgroundColor =
        ResourcesCompat.getColor(resources, R.color.design_default_color_background, null)

    init {
        isClickable = true
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

        val cx = 150F
        val cy = 800F
        val length = 600.0

        var startingAngle: Double = -9.0 - 90.0
        for (i: Int in 1..6) {
            var normalColor: Paint
            var specialColor: Paint

            if (i % 2 == 0) {
                normalColor = khakiField
                specialColor = greenFieldColor
            } else {
                normalColor = blackField
                specialColor = redField
            }

            for (j: Int in 0..5) {
                val rInn = getInnerRadius(length, j)
                val rOut = getROut(length, j)
                val color: Paint = when {
                    j == 5 -> blackField
                    j % 2 == 0 -> specialColor
                    else -> normalColor
                }

                arcSegmentFunctions.drawArcSegment(
                    canvas!!,
                    cx,
                    cy,
                    rInn.toFloat(),
                    rOut.toFloat(),
                    startingAngle,
                    18.0F,
                    color,
                    color
                )
            }

            startingAngle += 18.0
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
}