package com.jummania.analogue_watch

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.divider.MaterialDivider
import com.jummania.analogue_watch.databinding.ActivityMainBinding
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            val tik1 = MediaPlayer.create(this@MainActivity, R.raw.tik1)
            val tik2 = MediaPlayer.create(this@MainActivity, R.raw.tik2)

            runnable = object : Runnable {
                override fun run() {

                    val calendar = Calendar.getInstance()
                    val seconds = calendar.get(Calendar.SECOND)
                    val minutes = calendar.get(Calendar.MINUTE)

                    if (!tik1.isPlaying && !tik2.isPlaying) {
                        if (seconds % 2 == 0) tik2.start()
                        else tik1.start()
                    }

                    second.rotation = seconds * 6f
                    minute.rotation = (minutes + seconds / 60.0f) * 6f
                    hour.rotation = (calendar.get(Calendar.HOUR) + minutes / 60.0f) * 30f

                    handler.postDelayed(this, 1000)
                }
            }

            runnable?.let { handler.post(it) }

            val typedValue = TypedValue()
            theme.resolveAttribute(android.R.attr.colorSecondary, typedValue, true)
            val color = ContextCompat.getColor(this@MainActivity, typedValue.resourceId)

            for (i in 1..12) {
                val divider = MaterialDivider(this@MainActivity)

                val layoutParams = RelativeLayout.LayoutParams(
                    10, RelativeLayout.LayoutParams.MATCH_PARENT
                )
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
                divider.layoutParams = layoutParams
                divider.dividerColor = color
                divider.rotation = 30f * i
                mainCircle.addView(divider)
            }

        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        runnable?.let { handler.post(it) }
    }
}