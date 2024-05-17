package com.jummania.analogue_watch

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.divider.MaterialDivider
import com.jummania.analogue_watch.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            for (i in 1..12) {
                val divider = MaterialDivider(this@MainActivity)

                val layoutParams = RelativeLayout.LayoutParams(
                    10, RelativeLayout.LayoutParams.MATCH_PARENT
                )
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
                divider.layoutParams = layoutParams
                divider.dividerColor = Color.BLACK
                divider.rotation = 30f * i
                mainCircle.addView(divider)
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(object : Runnable {
                override fun run() {
                    val calendar = Calendar.getInstance()
                    val seconds = calendar.get(Calendar.SECOND)
                    val minutes = calendar.get(Calendar.MINUTE)

                    second.rotation = seconds * 6f
                    minute.rotation = (minutes + seconds / 60.0f) * 6f
                    hour.rotation = (calendar.get(Calendar.HOUR) + minutes / 60.0f) * 30f

                    handler.postDelayed(this, 1000)
                }
            }, 0)

        }
    }
}