package com.jummania

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.RawRes
import androidx.core.graphics.ColorUtils
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


/**
 *  * Created by Jummania on 23,May,2024.
 *  * Email: sharifuddinjumman@gmail.com
 *  * Dhaka, Bangladesh.
 */
class AnalogClock @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f

    private var backgroundColor = getColor("#FEF7FF")
    private var minuteMarkerColor = getColor("#1D1B20")
    private var hourMarkerColor = getColor("#C90000")

    private var secondHandColor = hourMarkerColor
    private var minuteHandColor = minuteMarkerColor
    private var hourHandColor = minuteMarkerColor

    private var minuteMarkerHeight = 0.05f
    private var hourMarkerHeight = 0.1f

    private var minuteMarker = true
    private var hourMarker = true

    private var secondHandHeight = 0.8f
    private var minuteHandHeight = 0.6f
    private var hourHandHeight = 0.5f

    private var secondHandWidth = 0.02f
    private var minuteHandWidth = 0.03f
    private var hourHandWidth = 0.04f

    private var tik1 = getMediaPlayer(R.raw.tik1)
    private var tik2 = getMediaPlayer(R.raw.tik2)
    private var sound = true
    private var volume = 1f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        centerX = width / 2f
        centerY = height / 2f
        radius = min(centerX, centerY)

        drawCircle(canvas, radius, backgroundColor)
        createMarker(canvas)
        createHand(canvas)

        val minSize = radius * 0.04f
        val darkerColor = ColorUtils.blendARGB(backgroundColor, 0xFF000000.toInt(), 0.3f)
        drawCircle(canvas, minSize, darkerColor)
        drawCircle(
            canvas, minSize / 2, ColorUtils.blendARGB(backgroundColor, 0xFFFFFFFF.toInt(), 0.3f)
        )
        postInvalidateDelayed(1000)

    }

    private fun drawCircle(canvas: Canvas, radius: Float, backgroundColor: Int) {
        paint.color = backgroundColor
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    private fun createMarker(canvas: Canvas) {
        paint.strokeWidth = 3f
        for (i in 1..60) {
            val angle = i * 6f

            if (i % 5 == 0 && hourMarker) {
                paint.color = hourMarkerColor
                createMarker(canvas, radius * hourMarkerHeight, angle)
            } else if (minuteMarker) {
                paint.color = minuteMarkerColor
                createMarker(canvas, radius * minuteMarkerHeight, angle)
            }
        }
    }

    private fun createMarker(canvas: Canvas, length: Float, angle: Float) {
        val markerStartX = centerX + (radius - length) * cos(Math.toRadians(angle.toDouble()))
        val markerStartY = centerY + (radius - length) * sin(Math.toRadians(angle.toDouble()))
        val markerEndX = centerX + radius * cos(Math.toRadians(angle.toDouble()))
        val markerEndY = centerY + radius * sin(Math.toRadians(angle.toDouble()))
        canvas.drawLine(
            markerStartX.toFloat(),
            markerStartY.toFloat(),
            markerEndX.toFloat(),
            markerEndY.toFloat(),
            paint
        )

    }

    private fun createHand(canvas: Canvas) {
        val calendar = Calendar.getInstance()
        val seconds = calendar.get(Calendar.SECOND)
        val minutes = calendar.get(Calendar.MINUTE) + seconds / 60.0f


        if (!tik1.isPlaying && !tik2.isPlaying && sound) {
            if (seconds % 2 == 0) tik2.start()
            else tik1.start()
        }


        createHand(
            canvas,
            radius * hourHandWidth,
            radius * hourHandHeight,
            0.06f,
            hourHandColor,
            (calendar.get(Calendar.HOUR) + minutes / 60.0f) * 5
        )
        createHand(
            canvas,
            radius * minuteHandWidth,
            radius * minuteHandHeight,
            0.08f,
            minuteHandColor,
            minutes
        )
        createHand(
            canvas,
            radius * secondHandWidth,
            radius * secondHandHeight,
            0.2f,
            secondHandColor,
            seconds.toFloat()
        )
    }

    private fun createHand(
        canvas: Canvas,
        width: Float,
        height: Float,
        extraLine: Float,
        backgroundColor: Int,
        angle: Float
    ) {
        paint.color = backgroundColor
        paint.strokeWidth = width

        // Convert angle to radians
        val mAngle = Math.PI * angle / 30 - Math.PI / 2

        // Calculate the end point of the hand
        val endX = (centerX + cos(mAngle) * height).toFloat()
        val endY = (centerY + sin(mAngle) * height).toFloat()

        // Calculate the starting point of the line behind the center
        val startX =
            (centerX - cos(mAngle) * (height * extraLine)).toFloat() // Adjust the multiplier to control the length of the extra line
        val startY =
            (centerY - sin(mAngle) * (height * extraLine)).toFloat() // Adjust the multiplier to control the length of the extra line

        // Draw the line behind the center
        canvas.drawLine(centerX, centerY, startX, startY, paint)

        // Draw the main hand
        canvas.drawLine(centerX, centerY, endX, endY, paint)
    }


    private fun getColor(colorCode: String): Int {
        return Color.parseColor(colorCode)
    }

    private fun setBackgroundColor(colorCode: String) {
        backgroundColor = getColor(colorCode)
    }

    private fun setMarkerColor(
        minuteMarkerColor: Int = this.minuteMarkerColor, hourMarkerColor: Int = this.hourMarkerColor
    ) {
        this.minuteMarkerColor = minuteMarkerColor
        this.hourMarkerColor = hourMarkerColor
    }

    fun setMarkerHeight(
        @FloatRange(from = 0.0, to = 1.0) minuteMarkerHeight: Float = this.minuteMarkerHeight,
        @FloatRange(from = 0.0, to = 1.0) hourMarkerHeight: Float = this.hourMarkerHeight
    ) {
        this.minuteMarkerHeight = minuteMarkerHeight
        this.hourMarkerHeight = hourMarkerHeight
    }

    fun setHandHeight(
        @FloatRange(from = 0.0, to = 1.0) secondHandHeight: Float = this.secondHandHeight,
        @FloatRange(from = 0.0, to = 1.0) minuteHandHeight: Float = this.minuteHandHeight,
        @FloatRange(from = 0.0, to = 1.0) hourHandHeight: Float = this.hourHandHeight
    ) {
        this.secondHandHeight = secondHandHeight
        this.minuteHandHeight = minuteHandHeight
        this.hourHandHeight = hourHandHeight
    }

    fun setHandWidth(
        @FloatRange(from = 0.0, to = 1.0) secondHandWidth: Float = this.secondHandWidth,
        @FloatRange(from = 0.0, to = 1.0) minuteHandWidth: Float = this.minuteHandWidth,
        @FloatRange(from = 0.0, to = 1.0) hourHandWidth: Float = this.hourHandWidth
    ) {
        this.secondHandWidth = secondHandWidth
        this.hourHandWidth = hourHandWidth
        this.hourHandWidth = hourHandWidth
    }

    private fun setHandColor(
        secondHandColor: Int = this.secondHandColor,
        minuteHandColor: Int = this.minuteHandColor,
        hourHandColor: Int = this.hourHandColor
    ) {
        this.secondHandColor = secondHandColor
        this.minuteHandColor = minuteHandColor
        this.hourHandColor = hourHandColor
    }

    private fun enableMarkers(
        minuteMarker: Boolean = this.minuteMarker, hourMarker: Boolean = this.hourMarker
    ) {
        this.minuteMarker = minuteMarker
        this.hourMarker = hourMarker
    }

    private fun getMediaPlayer(@RawRes res: Int): MediaPlayer {
        return MediaPlayer.create(context, res).also {
            it.setVolume(volume, volume)
        }
    }

    fun setMediaPlayer(mediaPlayer1: MediaPlayer = tik1, mediaPlayer2: MediaPlayer = tik2) {
        tik1 = mediaPlayer1
        tik2 = mediaPlayer2
    }

    fun enableSound(boolean: Boolean) {
        sound = boolean
    }

    fun setVolume(@FloatRange(from = 0.0, to = 1.0) volume: Float) {
        this.volume = volume
        tik1.setVolume(volume, volume)
        tik2.setVolume(volume, volume)
    }

}




