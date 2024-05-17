package com.jummania.analogue_watch

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

            runnable = object : Runnable {
                override fun run() {
                    val calendar = Calendar.getInstance()
                    val seconds = calendar.get(Calendar.SECOND)
                    val minutes = calendar.get(Calendar.MINUTE)

                    second.rotation = seconds * 6f
                    minute.rotation = (minutes + seconds / 60.0f) * 6f
                    hour.rotation = (calendar.get(Calendar.HOUR) + minutes / 60.0f) * 30f

                    handler.postDelayed(this, 1000)
                }
            }

            runnable?.let { handler.post(it) }

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