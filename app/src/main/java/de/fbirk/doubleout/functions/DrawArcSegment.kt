package de.fbirk.doubleout.functions

import android.graphics.*
import android.graphics.Paint.Align
import kotlin.math.cos
import kotlin.math.sin


public class DrawArcSegment {
    private val CIRCLE_LIMIT = 359.9999f

    /**
     * Draws a thick arc between the defined angles, see {@link Canvas#drawArc} for more.
     * This method is equivalent to
     * <pre><code>
     * float rMid = (rInn + rOut) / 2;
     * paint.setStyle(Style.STROKE); // there's nothing to fill
     * paint.setStrokeWidth(rOut - rInn); // thickness
     * canvas.drawArc(new RectF(cx - rMid, cy - rMid, cx + rMid, cy + rMid), startAngle, sweepAngle, false, paint);
     * </code></pre>
     * but supports different fill and stroke paints.
     * <a href="https://stackoverflow.com/a/25228407" />
     *
     * @param canvas
     * @param cx horizontal middle point of the oval
     * @param cy vertical middle point of the oval
     * @param rInn inner radius of the arc segment
     * @param rOut outer radius of the arc segment
     * @param startAngle see {@link Canvas#drawArc}
     * @param sweepAngle see {@link Canvas#drawArc}, capped at &plusmn;360
     * @param fill filling paint, can be <code>null</code>
     * @param stroke stroke paint, can be <code>null</code>
     * @see Canvas#drawArc
     */
    public fun drawArcSegment(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        rInn: Float,
        rOut: Float,
        startAngle: Double,
        sweepAngle: Float,
        fill: Paint,
        stroke: Paint? = null
    ): Path {
        var sweepAngle = sweepAngle
        if (sweepAngle > CIRCLE_LIMIT) {
            sweepAngle = CIRCLE_LIMIT
        }
        if (sweepAngle < -CIRCLE_LIMIT) {
            sweepAngle = -CIRCLE_LIMIT
        }

        val outerRect = RectF(cx - rOut, cy - rOut, cx + rOut, cy + rOut)
        val innerRect = RectF(cx - rInn, cy - rInn, cx + rInn, cy + rInn)

        val segmentPath = Path()
        val start: Double = Math.toRadians(startAngle)
        segmentPath.moveTo(
            (cx + rInn * cos(start)).toFloat(),
            (cy + rInn * sin(start)).toFloat()
        )
        segmentPath.lineTo(
            (cx + rOut * cos(start)).toFloat(),
            (cy + rOut * sin(start)).toFloat()
        )
        segmentPath.arcTo(outerRect, startAngle.toFloat(), sweepAngle)
        val end: Double = Math.toRadians((startAngle + sweepAngle))
        segmentPath.lineTo(
            (cx + rInn * cos(end)).toFloat(),
            (cy + rInn * sin(end)).toFloat()
        )
        segmentPath.arcTo(innerRect, startAngle.toFloat() + sweepAngle, -sweepAngle)
        canvas.drawPath(segmentPath, fill)
        if (stroke != null) {
            stroke.style = Paint.Style.STROKE
            canvas.drawPath(segmentPath, stroke)
        }
        return segmentPath
    }

    public fun drawText(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        rInn: Float,
        rOut: Float,
        startAngle: Double,
        sweepAngle: Float,
        text: String,
        textPaint: Paint
    ) {
        textPaint.textAlign = Align.CENTER
        val midway = Path()
        val r: Float = (rInn + rOut) / 2
        val segment = RectF(cx - r, cy - r, cx + r, cy + r)
        midway.addArc(segment, startAngle.toFloat(), sweepAngle)
        canvas.drawTextOnPath(text, midway, 0F, 0F, textPaint)
    }

}