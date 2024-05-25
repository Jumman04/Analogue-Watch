package com.jummania

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.content.res.use
import androidx.core.graphics.ColorUtils
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * An AnalogClock view for displaying time in a traditional analog format. This class provides a customizable
 * representation of time, allowing developers to adjust various aspects such as hour markers, minute markers,
 * hour indicator text, and clock hands (hour, minute, second) to suit their application's design.
 *
 * The AnalogClock class encapsulates the visual representation of a clock face and hands, providing a visually
 * appealing and intuitive way to display time to users. Developers can easily integrate this view into their
 * applications and customize its appearance to match the overall theme and style.
 *
 * This class offers a range of customizable attributes, including:
 * - Background color of the clock face
 * - Colors of minute markers, hour markers, and clock hands
 * - Heights and widths of minute markers, hour markers, and clock hands
 * - Enabling or disabling display of minute markers, hour markers, and hour indicator text
 * - Enabling or disabling display of clock hands (hour, minute, second)
 * - Adjusting volume and selecting sound for ticking effects
 * - Customizing text size, font family, text color, and text style for hour indicator text
 *
 * Developers can instantiate an instance of AnalogClock with various constructors, allowing for easy integration
 * into layout files and programmatic usage within activities or fragments. By utilizing the provided attributes
 * and methods, developers can create highly customizable and visually appealing clock displays tailored to
 * their application's requirements.
 *
 * @property context The context in which the view is created.
 * @property attrs The set of attributes defined in XML for customization.
 * @property defStyleAttr An attribute in the current theme that contains a reference to a style resource.
<p>
 *  * Created by Jummania on 23,May,2024.
 *  * Email: sharifuddinjumman@gmail.com
 *  * Dhaka, Bangladesh.
 */
class AnalogClock @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Paint and Rect Objects
    private val paint = Paint() // Used for drawing shapes and text on the canvas.
    private val rect by lazy { Rect() } // Used for calculating the size and position of drawing elements.

    // Integer Variable
    private var seconds = 0 // Current value of seconds on the clock face.

    // Float Variables
    private var centerX = 0f // X-coordinate of the center of the clock.
    private var centerY = 0f // Y-coordinate of the center of the clock.
    private var radius = 0f // Radius of the clock face.
    private var minuteMarkerHeight =
        setValue(0.05f) // Height of the minute markers relative to the clock radius.
    private var hourMarkerHeight =
        setValue(0.1f) // Height of the hour markers relative to the clock radius.
    private var secondHandHeight =
        setValue(0.8f) // Length of the second hand relative to the clock radius.
    private var minuteHandHeight =
        setValue(0.6f) // Length of the minute hand relative to the clock radius.
    private var hourHandHeight =
        setValue(0.5f) // Length of the hour hand relative to the clock radius.
    private var secondHandWidth = setValue(0.02f) // Width of the second hand.
    private var minuteHandWidth = setValue(0.03f) // Width of the minute hand.
    private var hourHandWidth = setValue(0.04f) // Width of the hour hand.
    private var textSize = 22f.toSP() // Size of the clock text.
    private var volume = 0.1f // Volume level for the clock ticking sound.

    // Integer (Color) Variables
    private var backgroundColor = getColor("#FEF7FF") // Background color of the clock face.
    private var minuteMarkerColor = getColor("#1D1B20") // Color of the minute markers.
    private var hourMarkerColor = getColor("#C90000") // Color of the hour markers.
    private var secondHandColor = hourMarkerColor // Color of the second hand.
    private var minuteHandColor = minuteMarkerColor // Color of the minute hand.
    private var hourHandColor = minuteMarkerColor // Color of the hour hand.
    private var textColor = Color.BLACK // Color of the clock text.

    // Boolean Variables
    private var minuteMarker = true // Flag to show/hide minute markers.
    private var hourMarker = true // Flag to show/hide hour markers.
    private var hourText = true // Flag to show/hide hour numbers.
    private var secondHand = true // Flag to show/hide the second hand.
    private var minuteHand = true // Flag to show/hide the minute hand.
    private var hourHand = true // Flag to show/hide the hour hand.
    private var sound = true // Flag to enable/disable clock ticking sound.

    // Typeface Variable
    private var typeface: Typeface? = null // Typeface for the clock text.

    // MediaPlayer Variable
    private var tik1 =
        getMediaPlayer(R.raw.sound) // MediaPlayer for playing the clock ticking sound.
        set(value) { // Setter for tik1 MediaPlayer.
            tik1?.stop() // Stop the currently playing sound.
            tik1?.release() // Release system resources associated with the MediaPlayer.
            field = value // Assign the new MediaPlayer to the property.
        }


    /**
     * Handler to manage the timing of the clock updates, running on the main UI thread.
     */
    private val handler = Handler(Looper.getMainLooper())


    /**
     * Runnable that defines the task to be repeatedly executed to update the clock.
     */
    private var runnable: Runnable? = null


    // Initialization block to apply custom attributes from XML
    init {
        // Obtain the styled attributes from the XML attributes
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.AnalogClock, defStyleAttr, defStyleAttr
        ).use { typedArray ->
            // Set background color
            setBackgroundColor(
                typedArray.getColor(
                    R.styleable.AnalogClock_background_color, backgroundColor
                )
            )
            // Set marker colors
            setMarkerColor(
                typedArray.getColor(
                    R.styleable.AnalogClock_minute_marker_color, minuteMarkerColor
                ), typedArray.getColor(
                    R.styleable.AnalogClock_hour_marker_color, hourMarkerColor
                )
            )
            // Set hand colors
            setHandColor(
                typedArray.getColor(
                    R.styleable.AnalogClock_second_hand_color, secondHandColor
                ), typedArray.getColor(
                    R.styleable.AnalogClock_minute_hand_color, minuteHandColor
                ), typedArray.getColor(
                    R.styleable.AnalogClock_hour_hand_color, hourHandColor
                )
            )
            // Set marker heights
            setMarkerHeight(
                typedArray.getFloat(
                    R.styleable.AnalogClock_minute_marker_height, minuteMarkerHeight
                ), typedArray.getFloat(
                    R.styleable.AnalogClock_hour_marker_height, hourMarkerHeight
                )
            )
            // Set hand lengths
            setHandHeight(
                typedArray.getFloat(
                    R.styleable.AnalogClock_second_hand_height, secondHandHeight
                ), typedArray.getFloat(
                    R.styleable.AnalogClock_minute_hand_height, minuteHandHeight
                ), typedArray.getFloat(
                    R.styleable.AnalogClock_hour_hand_height, hourHandHeight
                )
            )
            // Set hand widths
            setHandWidth(
                typedArray.getFloat(
                    R.styleable.AnalogClock_second_hand_width, secondHandWidth
                ), typedArray.getFloat(
                    R.styleable.AnalogClock_minute_hand_width, minuteHandWidth
                ), typedArray.getFloat(
                    R.styleable.AnalogClock_hour_hand_width, hourHandWidth
                )
            )
            // Enable/disable markers
            enableMarkers(
                typedArray.getBoolean(
                    R.styleable.AnalogClock_minute_marker, minuteMarker
                ), typedArray.getBoolean(
                    R.styleable.AnalogClock_hour_marker, hourMarker
                )
            )
            // Enable/disable hands
            enableHands(
                typedArray.getBoolean(
                    R.styleable.AnalogClock_enable_second_hand, secondHand
                ), typedArray.getBoolean(
                    R.styleable.AnalogClock_enable_minute_hand, minuteHand
                ), typedArray.getBoolean(
                    R.styleable.AnalogClock_enable_hour_hand, hourHand
                )
            )
            // Enable/disable sound
            enableSound(
                typedArray.getBoolean(
                    R.styleable.AnalogClock_enable_sound, sound
                )
            )
            // Set volume
            setVolume(
                typedArray.getFloat(
                    R.styleable.AnalogClock_volume, volume
                )
            )
            // Set MediaPlayer
            setMediaPlayer(
                typedArray.getResourceId(
                    R.styleable.AnalogClock_clock_sound, R.raw.sound
                )
            )
            // Set Typeface
            setTypeface(
                Typeface.create(
                    typedArray.getString(R.styleable.AnalogClock_fontFamily),
                    typedArray.getInt(R.styleable.AnalogClock_textStyle, 0)
                )
            )
            // Set text color
            setTextColor(
                typedArray.getColor(
                    R.styleable.AnalogClock_textColor, textColor
                )
            )
            // Enable/disable hour text
            enableHourText(
                typedArray.getBoolean(
                    R.styleable.AnalogClock_enable_hour_text, hourText
                )
            )
            // Set text size
            textSize = typedArray.getDimension(
                R.styleable.AnalogClock_textSize, textSize
            )
        }

        // Define the Runnable that will handle the clock updates
        runnable = object : Runnable {
            override fun run() {
                // Remove any pending posts of the Runnable from the message queue
                removeCallbacks(this)
                // Update the clock face by invalidating the view, causing a redraw
                invalidate()
                // Schedule the next redraw in 1000 milliseconds (1 second)
                handler.postDelayed(this, 1000)
            }
        }

        // If the runnable is not null, schedule the first redraw in 1000 milliseconds
        runnable?.let {
            handler.postDelayed(it, 1000)
        }

    }


    /**
     * This method is called when the view should render its content. It draws the analog clock face,
     * markers, and hands on the canvas.
     *
     * @param canvas The canvas on which the clock face, markers, and hands are drawn.
     */
    override fun onDraw(canvas: Canvas) {
        // Calculate center and radius of the clock face
        centerX = width / 2f
        centerY = height / 2f
        radius = min(centerX, centerY)

        // Draw the clock face
        drawCircle(canvas, radius, backgroundColor)

        // Create and draw markers (hour and minute)
        createMarker(canvas)

        // Create and draw hands (hour, minute, and optionally second)
        createHand(canvas)

        // If at least one hand is being drawn, add additional visual elements
        if (secondHand || minuteHand || hourHand) {
            // Draw additional circles for aesthetic purposes
            val minSize = radius * 0.04f
            val darkerColor = ColorUtils.blendARGB(backgroundColor, 0xFF000000.toInt(), 0.3f)
            drawCircle(canvas, minSize, darkerColor)
            drawCircle(
                canvas, minSize / 2, ColorUtils.blendARGB(backgroundColor, 0xFFFFFFFF.toInt(), 0.3f)
            )
        }
    }


    /**
     * Called when the visibility of this view and its ancestors change.
     * This is used to start or stop the clock updates based on the view's visibility.
     *
     * @param isVisible True if this view and its ancestors are visible, false otherwise.
     */
    override fun onVisibilityAggregated(isVisible: Boolean) {
        super.onVisibilityAggregated(isVisible)
        if (isVisible) {
            // If the view is visible, start the Runnable to update the clock every second
            runnable?.let {
                handler.postDelayed(it, 1000)
            }
        } else {
            // If the view is not visible, remove any pending callbacks to stop updates
            handler.removeCallbacksAndMessages(null)
        }
    }


    /**
     * Draws a circle on the canvas with the specified radius and background color.
     *
     * @param canvas The canvas on which the circle will be drawn.
     * @param radius The radius of the circle.
     * @param backgroundColor The background color of the circle.
     */
    private fun drawCircle(canvas: Canvas, radius: Float, backgroundColor: Int) {
        paint.color = backgroundColor // Set the paint color to the specified background color
        canvas.drawCircle(centerX, centerY, radius, paint) // Draw a circle on the canvas
    }


    /**
     * Creates and draws markers (hour and minute) on the clock face.
     *
     * @param canvas The canvas on which the markers will be drawn.
     */
    private fun createMarker(canvas: Canvas) {
        paint.textSize = textSize * radius * 0.003f // Set the text size for drawing hour numbers

        val padding =
            radius - (radius * hourMarkerHeight + paint.textSize / 1.5f) // Calculate padding for hour numbers

        // Iterate through all 60 markers (1 to 60)
        for (i in 1..60) {
            val angle = i * 6f // Calculate the angle for each marker (6 degrees increment)

            // Check if the marker represents an hour (i.e., divisible by 5)
            if (i % 5 == 0) {
                // Draw hour marker if enabled
                if (hourMarker) {
                    paint.color = hourMarkerColor // Set the color for hour markers
                    paint.strokeWidth = 5f // Set the stroke width for drawing lines
                    createMarker(canvas, radius * hourMarkerHeight, angle) // Draw hour marker
                } else createMinuteMarker(canvas, angle) // Draw minute marker

                // Draw hour text if enabled
                if (hourText) {
                    val hourText: String = (i / 5).toString() // Convert marker index to hour
                    paint.getTextBounds(
                        hourText, 0, hourText.length, rect
                    ) // Calculate text bounding box
                    paint.typeface = typeface // Set the typeface for hour numbers
                    paint.color = textColor // Set the color for hour numbers

                    // Calculate position for drawing hour number
                    val mAngle = Math.PI * i / 30 - Math.PI / 2
                    val textX = centerX + cos(mAngle) * padding - rect.width() / 2
                    val textY = centerY + sin(mAngle) * padding + rect.height() / 2

                    // Draw hour number on the canvas
                    canvas.drawText(hourText, textX.toFloat(), textY.toFloat(), paint)
                }
            } else createMinuteMarker(canvas, angle) // Draw minute marker
        }
    }


    /**
     * Draws a minute marker on the canvas at the specified angle.
     *
     * @param canvas The Canvas on which the minute marker will be drawn.
     * @param angle The angle at which the minute marker will be drawn, in degrees.
     */
    private fun createMinuteMarker(canvas: Canvas, angle: Float) {
        if (minuteMarker) { // Draw minute marker if enabled
            paint.color = minuteMarkerColor // Set the color for minute markers
            paint.strokeWidth = 3f // Set the stroke width for drawing lines
            createMarker(canvas, radius * minuteMarkerHeight, angle) // Draw minute marker
        }
    }


    /**
     * Draws a marker line on the canvas with the specified length and angle.
     *
     * @param canvas The canvas on which the marker line will be drawn.
     * @param length The length of the marker line.
     * @param angle The angle at which the marker line will be drawn.
     */
    private fun createMarker(canvas: Canvas, length: Float, angle: Float) {
        // Calculate start and end points of the marker line
        val markerStartX = centerX + (radius - length) * cos(Math.toRadians(angle.toDouble()))
        val markerStartY = centerY + (radius - length) * sin(Math.toRadians(angle.toDouble()))
        val markerEndX = centerX + radius * cos(Math.toRadians(angle.toDouble()))
        val markerEndY = centerY + radius * sin(Math.toRadians(angle.toDouble()))

        // Draw the marker line on the canvas
        canvas.drawLine(
            markerStartX.toFloat(),
            markerStartY.toFloat(),
            markerEndX.toFloat(),
            markerEndY.toFloat(),
            paint
        )
    }


    /**
     * Creates and draws clock hands (hour, minute, and optionally second) on the canvas.
     *
     * @param canvas The canvas on which the clock hands will be drawn.
     * @return A boolean value indicating whether the clock should be redrawn.
     */
    private fun createHand(canvas: Canvas) {
        val calendar = Calendar.getInstance()

        val seconds = calendar.get(Calendar.SECOND)
        val minutes = calendar.get(Calendar.MINUTE) + seconds / 60.0f

        // Draw hour hand if enabled
        if (hourHand) {
            createHand(
                canvas,
                radius * hourHandWidth,
                radius * hourHandHeight,
                0.06f,
                hourHandColor,
                (calendar.get(Calendar.HOUR) + minutes / 60.0f) * 5
            )
        }

        // Draw minute hand if enabled
        if (minuteHand) {
            createHand(
                canvas,
                radius * minuteHandWidth,
                radius * minuteHandHeight,
                0.08f,
                minuteHandColor,
                minutes
            )
        }

        // Draw second hand if enabled
        if (secondHand) {

            // Update seconds and play ticking sound if necessary
            if (this.seconds != seconds) {
                this.seconds = seconds
                if (tik1?.isPlaying == false && sound) {
                    tik1?.start()
                }
            }

            createHand(
                canvas,
                radius * secondHandWidth,
                radius * secondHandHeight,
                0.2f,
                secondHandColor,
                seconds.toFloat()
            )
        }
    }


    /**
     * Draws a clock hand on the canvas with the specified width, height, extra line length, color, and angle.
     *
     * @param canvas The canvas on which the clock hand will be drawn.
     * @param width The width of the clock hand.
     * @param height The height of the clock hand.
     * @param extraLine The length of the extra line behind the center.
     * @param backgroundColor The background color of the clock hand.
     * @param angle The angle at which the clock hand will be drawn.
     */
    private fun createHand(
        canvas: Canvas,
        width: Float,
        height: Float,
        extraLine: Float,
        backgroundColor: Int,
        angle: Float
    ) {
        paint.color = backgroundColor // Set the color of the clock hand
        paint.strokeWidth = width // Set the width of the clock hand

        // Convert angle to radians
        val mAngle = Math.PI * angle / 30 - Math.PI / 2

        // Calculate the end point of the clock hand
        val endX = (centerX + cos(mAngle) * height).toFloat()
        val endY = (centerY + sin(mAngle) * height).toFloat()

        // Calculate the starting point of the line behind the center
        val startX =
            (centerX - cos(mAngle) * (height * extraLine)).toFloat() // Adjust the multiplier to control the length of the extra line
        val startY =
            (centerY - sin(mAngle) * (height * extraLine)).toFloat() // Adjust the multiplier to control the length of the extra line

        // Draw the line behind the center
        canvas.drawLine(centerX, centerY, startX, startY, paint)

        // Draw the main clock hand
        canvas.drawLine(centerX, centerY, endX, endY, paint)
    }


    /**
     * Converts a color code represented as a string to its corresponding integer value.
     *
     * @param colorCode The color code string to be converted.
     * @return The integer representation of the color.
     */
    private fun getColor(colorCode: String): Int {
        return Color.parseColor(colorCode)
    }


    /**
     * Sets the color of minute and hour markers.
     *
     * @param minuteMarkerColor The color of minute markers.
     * @param hourMarkerColor The color of hour markers.
     */
    fun setMarkerColor(
        minuteMarkerColor: Int = this.minuteMarkerColor, hourMarkerColor: Int = this.hourMarkerColor
    ) {
        this.minuteMarkerColor = minuteMarkerColor
        this.hourMarkerColor = hourMarkerColor
    }


    /**
     * Sets the color of second, minute, and hour hands.
     *
     * @param secondHandColor The color of the second hand.
     * @param minuteHandColor The color of the minute hand.
     * @param hourHandColor The color of the hour hand.
     */
    fun setHandColor(
        secondHandColor: Int = this.secondHandColor,
        minuteHandColor: Int = this.minuteHandColor,
        hourHandColor: Int = this.hourHandColor
    ) {
        this.secondHandColor = secondHandColor
        this.minuteHandColor = minuteHandColor
        this.hourHandColor = hourHandColor
    }


    /**
     * Creates a MediaPlayer object using the provided resource ID and sets its volume.
     *
     * @param res The resource ID of the sound file to be played.
     * @return The MediaPlayer object.
     */
    fun getMediaPlayer(res: Int): MediaPlayer? {
        return MediaPlayer.create(context, res)?.also {
            it.setVolume(volume, volume)
        }
    }


    /**
     * Sets the background color of the clock face.
     *
     * @param color The background color to be set.
     */
    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
    }


    /**
     * Sets the text color for the clock.
     *
     * @param color The text color to be set.
     */
    fun setTextColor(@ColorInt color: Int) {
        textColor = color
    }


    /**
     * Sets the height of minute and hour markers relative to the clock face radius.
     *
     * @param minuteMarkerHeight The height of minute markers as a fraction of the clock face radius.
     * @param hourMarkerHeight The height of hour markers as a fraction of the clock face radius.
     */
    fun setMarkerHeight(
        @FloatRange(from = 0.0, to = 1.0) minuteMarkerHeight: Float = this.minuteMarkerHeight,
        @FloatRange(from = 0.0, to = 1.0) hourMarkerHeight: Float = this.hourMarkerHeight
    ) {
        this.minuteMarkerHeight = setValue(minuteMarkerHeight)
        this.hourMarkerHeight = setValue(hourMarkerHeight)
    }


    /**
     * Sets the height of second, minute, and hour hands relative to the clock face radius.
     *
     * @param secondHandHeight The height of the second hand as a fraction of the clock face radius.
     * @param minuteHandHeight The height of the minute hand as a fraction of the clock face radius.
     * @param hourHandHeight The height of the hour hand as a fraction of the clock face radius.
     */
    fun setHandHeight(
        @FloatRange(from = 0.0, to = 1.0) secondHandHeight: Float = this.secondHandHeight,
        @FloatRange(from = 0.0, to = 1.0) minuteHandHeight: Float = this.minuteHandHeight,
        @FloatRange(from = 0.0, to = 1.0) hourHandHeight: Float = this.hourHandHeight
    ) {
        this.secondHandHeight = setValue(secondHandHeight)
        this.minuteHandHeight = setValue(minuteHandHeight)
        this.hourHandHeight = setValue(hourHandHeight)
    }


    /**
     * Sets the width of second, minute, and hour hands relative to the clock face radius.
     *
     * @param secondHandWidth The width of the second hand as a fraction of the clock face radius.
     * @param minuteHandWidth The width of the minute hand as a fraction of the clock face radius.
     * @param hourHandWidth The width of the hour hand as a fraction of the clock face radius.
     */
    fun setHandWidth(
        @FloatRange(from = 0.0, to = 1.0) secondHandWidth: Float = this.secondHandWidth,
        @FloatRange(from = 0.0, to = 1.0) minuteHandWidth: Float = this.minuteHandWidth,
        @FloatRange(from = 0.0, to = 1.0) hourHandWidth: Float = this.hourHandWidth
    ) {
        this.secondHandWidth = setValue(secondHandWidth)
        this.minuteHandWidth = setValue(minuteHandWidth)
        this.hourHandWidth = setValue(hourHandWidth)
    }


    /**
     * Sets the volume level for the ticking sound of the clock.
     * The volume level should be in the range of 0.0 to 1.0.
     *
     * @param volume The volume level to be set.
     */
    fun setVolume(@FloatRange(from = 0.0, to = 1.0) volume: Float) {
        // Ensure volume is within the valid range
        this.volume = if (volume > 1.0f) volume / 100.0f else volume
        // Set the volume for the ticking sound
        tik1?.setVolume(volume, volume)
    }


    /**
     * Sets the text size for the hour numbers displayed on the clock face.
     *
     * @param size The text size to be set.
     */
    fun setTextSize(size: Float) {
        textSize = size.toSP()
    }


    /**
     * Enables or disables the display of minute and hour markers on the clock face.
     *
     * @param minuteMarker Indicates whether minute markers should be displayed.
     * @param hourMarker Indicates whether hour markers should be displayed.
     */
    fun enableMarkers(
        minuteMarker: Boolean = this.minuteMarker, hourMarker: Boolean = this.hourMarker
    ) {
        this.minuteMarker = minuteMarker
        this.hourMarker = hourMarker
    }


    /**
     * Enables or disables the display of second, minute, and hour hands on the clock face.
     *
     * @param secondHand Indicates whether the second hand should be displayed.
     * @param minuteHand Indicates whether the minute hand should be displayed.
     * @param hourHand Indicates whether the hour hand should be displayed.
     */
    fun enableHands(
        secondHand: Boolean = this.secondHand,
        minuteHand: Boolean = this.minuteHand,
        hourHand: Boolean = this.hourHand
    ) {
        this.secondHand = secondHand
        this.minuteHand = minuteHand
        this.hourHand = hourHand
    }


    /**
     * Enables or disables the ticking sound of the clock.
     *
     * @param enabled Indicates whether the ticking sound should be enabled.
     */
    fun enableSound(enabled: Boolean) {
        sound = enabled
    }


    /**
     * Enables or disables the display of hour numbers on the clock face.
     *
     * @param enabled Indicates whether hour numbers should be displayed.
     */
    fun enableHourText(enabled: Boolean) {
        hourText = enabled
    }


    /**
     * Sets the MediaPlayer object used for playing clock ticking sound.
     *
     * @param mediaPlayer The MediaPlayer object to be set.
     */
    fun setMediaPlayer(mediaPlayer: MediaPlayer?) {
        tik1 = mediaPlayer
    }


    /**
     * Sets the MediaPlayer object for playing clock ticking sound using the resource ID.
     *
     * @param res The resource ID of the sound file to be played.
     */
    fun setMediaPlayer(res: Int) {
        tik1 = getMediaPlayer(res)
    }


    /**
     * Sets the typeface for hour numbers displayed on the clock face.
     *
     * @param typeface The Typeface object to be set.
     */
    fun setTypeface(typeface: Typeface?) {
        this.typeface = typeface
    }


    /**
     * Adjusts the value to ensure it is within the valid range of 0.0 to 1.0.
     *
     * @param value The value to be adjusted.
     * @return The adjusted value.
     */
    private fun setValue(value: Float): Float {
        return if (value > 1.0f) value / 100.0f else value
    }


    /**
     * Converts a size value from scaled pixels (SP) to pixels (PX).
     *
     * @param value The size value in scaled pixels (SP).
     * @return The size value in pixels (PX).
     */
    private fun Float.toSP(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, this, resources.displayMetrics
        )
    }

}




