package com.jummania.analogue_watch

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.color.MaterialColors
import com.jummania.analogue_watch.databinding.FragmentWatchBinding
import java.util.Calendar


class WatchFragment : Fragment() {

    private var binding: FragmentWatchBinding? = null
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val preferenceManager by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            val tik1 = getMediaPlayer(R.raw.tik1)
            val tik2 = getMediaPlayer(R.raw.tik2)

            second.isVisible = getBoolean("secondHand")
            minute.isVisible = getBoolean("minuteHand")
            hour.isVisible = getBoolean("hourHand")
            val sound = getBoolean("sound")

            runnable = object : Runnable {
                override fun run() {

                    val calendar = Calendar.getInstance()
                    val seconds = calendar.get(Calendar.SECOND)
                    val minutes = calendar.get(Calendar.MINUTE)

                    if (!tik1.isPlaying && !tik2.isPlaying && sound) {
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


            if (getBoolean("clockMarker")) {
                val color = getColor(android.R.attr.textColorPrimary, Color.BLACK)
                val redColor = getColor(R.attr.red, Color.RED)


                val layoutParams = RelativeLayout.LayoutParams(
                    9, RelativeLayout.LayoutParams.MATCH_PARENT
                )
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

                val hourMarker = getBoolean("hourMarker")
                val minuteMarker = getBoolean("minuteMarker")

                for (i in 1..60) {
                    val divider = LinearLayout(requireContext())

                    divider.layoutParams = layoutParams
                    divider.weightSum = 2f
                    divider.rotation = 6f * i
                    divider.orientation = LinearLayout.VERTICAL

                    val firstLinearLayout = getLayout()
                    val secondLinearLayout = getLayout()

                    if (i % 5 == 0 && hourMarker) {
                        firstLinearLayout.setBackgroundColor(redColor)
                        setWeight(firstLinearLayout, secondLinearLayout, 0.2f, 0.8f)
                    } else if (minuteMarker) {
                        firstLinearLayout.setBackgroundColor(color)
                        setWeight(firstLinearLayout, secondLinearLayout, 0.1f, 0.9f)
                    }

                    divider.addView(firstLinearLayout)
                    divider.addView(secondLinearLayout)

                    mainCircle.addView(divider)
                }
            }
        }

        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)?.supportActionBar?.let {
            it.title = "Analogue Watch"
            it.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun getMediaPlayer(@RawRes res: Int): MediaPlayer {
        return MediaPlayer.create(requireContext(), res).also {
            val volume = preferenceManager.getInt("volume", 0.1f.toInt()) / 10f
            it.setVolume(volume, volume)
        }
    }

    private fun getColor(color: Int, defaultColor: Int): Int {
        return MaterialColors.getColor(requireContext(), color, defaultColor)
    }

    private fun getLayout(): LinearLayout {
        return LinearLayout(requireContext()).also {
            it.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0
            )
        }
    }

    private fun setWeight(
        firstLinearLayout: LinearLayout,
        secondLinearLayout: LinearLayout,
        firstWeight: Float,
        secondWeight: Float
    ) {
        (firstLinearLayout.layoutParams as LinearLayout.LayoutParams).weight = firstWeight
        (secondLinearLayout.layoutParams as LinearLayout.LayoutParams).weight = secondWeight
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        runnable?.let { handler.post(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) parentFragmentManager.beginTransaction()
            .setReorderingAllowed(true).addToBackStack(null)
            .replace(R.id.fragmentContainerView, SettingsFragment()).commit()
        return super.onOptionsItemSelected(item)
    }

    private fun getBoolean(key: String): Boolean {
        return preferenceManager.getBoolean(key, true)
    }
}