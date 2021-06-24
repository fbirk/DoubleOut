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


class DartboardQuarterView @JvmOverloads constructor(
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

        val cx = 500F
        val cy = 500F
        val length = 340.0

        var startingAngle: Double = -90.0
        for (i: Int in 0..4) {
            var normalColor: Paint

            if (i % 2 == 0) {
                normalColor = greenFieldColor
            } else {
                normalColor = redField
            }

            val rInn = length * 0.3

            arcSegmentFunctions.drawArcSegment(
                canvas!!,
                cx,
                cy,
                rInn.toFloat(),
                length.toFloat(),
                startingAngle,
                90.0F,
                normalColor,
                normalColor
            )
            startingAngle += 90.0
        }

        // outer bulls eye
        arcSegmentFunctions.drawArcSegment(
            canvas!!,
            cx,
            cy,
            0.0F,
            (length * 0.3).toFloat(),
            0.0,
            360.0F,
            khakiField,
            khakiField
        )
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