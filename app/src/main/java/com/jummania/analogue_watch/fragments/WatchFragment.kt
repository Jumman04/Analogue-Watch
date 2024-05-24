package com.jummania.analogue_watch.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.jummania.analogue_watch.R
import com.jummania.analogue_watch.databinding.FragmentWatchBinding
import soup.neumorphism.NeumorphShapeAppearanceModel


class WatchFragment : Fragment() {

    private var binding: FragmentWatchBinding? = null

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

        val frame = getBoolean("frame")

        val primaryColor = getColor("primaryColor", "#1D1B20")
        val secondaryColor = getColor("secondaryColor", "#C90000")

        binding?.apply {
            val backgroundColor = getColor("backgroundColor", "#FEF7FF")
            clock.setBackgroundColor(backgroundColor)
            clock.setMarkerColor(primaryColor, secondaryColor)
            clock.setHandColor(secondaryColor, primaryColor, primaryColor)
            clock.setTextColor(getColor("textColor", "#000000"))

            clock.enableMarkers(getBoolean("minuteMarker"), getBoolean("hourMarker"))
            clock.enableHands(
                getBoolean("secondHand"), getBoolean("minuteHand"), getBoolean("hourHand")
            )
            clock.enableHourText(getBoolean("hourMarkerText"))
            clock.enableSound(getBoolean("sound"))

            clock.setVolume(preferenceManager.getInt("volume", 1) / 10f)

            if (!frame) {
                mainFrame.setShadowElevation(0f)
                secondFrame.setShadowElevation(0f)
                mainFrame.setShapeAppearanceModel(NeumorphShapeAppearanceModel.builder().build())
            }
        }


        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)?.supportActionBar?.let {
            it.title = "Analogue Watch"
            it.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun getColor(key: String, defaultColor: String): Int {
        return preferenceManager.getInt(key, Color.parseColor(defaultColor))
    }

    private fun getBoolean(key: String): Boolean {
        return preferenceManager.getBoolean(key, true)
    }

    /*
        private fun startClock() {
            binding?.apply {
                val tik1 = getMediaPlayer(R.raw.tik1)
                val tik2 = getMediaPlayer(R.raw.tik2)
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
            }


        }

        private fun setResponsive(relativeLayoutParam: RelativeLayout.LayoutParams, frame: Boolean) {
            binding?.apply {
                mainCircle.post {
                    mainFrame.visibility(true)
                    val width = mainCircle.width - mainCircle.paddingLeft - mainCircle.paddingRight

                    second.setHeight(width * 0.8f)
                    minute.setHeight(width * 0.7f)
                    hour.setHeight(width * 0.6f)

                    val circleWidth = width / 14f
                    circle.setHeight(circleWidth)
                    circle.setWidth(circleWidth)

                    val secondWidth = circleWidth / 4
                    second.setWidth(secondWidth)
                    minute.setWidth(circleWidth / 3)
                    hour.setWidth(circleWidth / 2)

                    relativeLayoutParam.width = (secondWidth * 0.8).toInt()

                    if (frame) {
                        val padding = (circleWidth * 0.6f).toInt()
                        mainFrame.setPadding(padding)
                        secondFrame.setPadding(padding)
                    }
                }
            }
        }

        private fun createMarker(
            relativeLayoutParam: RelativeLayout.LayoutParams, primaryColor: Int, secondaryColor: Int
        ) {
            binding?.apply {
                val hourMarker = getBoolean("hourMarker")
                val minuteMarker = getBoolean("minuteMarker")
                if (hourMarker || minuteMarker) {

                    relativeLayoutParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

                    for (i in 1..60) {
                        val divider = LinearLayout(requireContext())

                        divider.layoutParams = relativeLayoutParam
                        divider.weightSum = 2f
                        divider.rotation = 6f * i
                        divider.orientation = LinearLayout.VERTICAL
                        divider.elevation = 0f

                        val markers = LinearLayout(requireContext())

                        if (i % 5 == 0 && hourMarker) {
                            markers.setBackgroundColor(secondaryColor)
                            markers.setWeight(0.2f)
                        } else if (minuteMarker) {
                            markers.setBackgroundColor(primaryColor)
                            markers.setWeight(0.1f)
                        }

                        divider.addView(markers)
                        mainCircle.addView(divider)
                    }

                }
            }
        }

        private fun visibility(frame: Boolean) {
            val secondHand = getBoolean("secondHand")
            val minuteHand = getBoolean("minuteHand")
            val hourHand = getBoolean("hourHand")

            binding?.apply {
                second.visibility(secondHand)
                minute.visibility(minuteHand)
                hour.visibility(hourHand)
                circle.visibility(secondHand || minuteHand || hourHand)


                if (!frame) {
                    mainFrame.setShadowElevation(0f)
                    secondFrame.setShadowElevation(0f)
                    mainFrame.setPadding(0)
                    secondFrame.setPadding(0)
                    mainFrame.setShapeAppearanceModel(NeumorphShapeAppearanceModel.builder().build())
                }
            }
        }

        private fun getMediaPlayer(@RawRes res: Int): MediaPlayer {
            return MediaPlayer.create(requireContext(), res).also {
                val volume = preferenceManager.getInt("volume", 1) / 10f
                it.setVolume(volume, volume)
            }
        }

        private fun getColor(key: String, defaultColor: String): Int {
            return preferenceManager.getInt(key, Color.parseColor(defaultColor))
        }

        private fun LinearLayout.setWeight(weight: Float) {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, weight
            )
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

        private fun View.visibility(boolean: Boolean) {
            animate().alpha(if (boolean) 1f else 0f).start()
        }

        private fun View.setHeight(height: Float) {
            layoutParams = layoutParams.also { it.height = height.toInt() }
        }

        private fun View.setWidth(width: Float) {
            layoutParams = layoutParams.also { it.width = width.toInt() }
        }

        private fun View.setPadding(padding: Int) {
            setPadding(padding, padding, padding, padding)
        }

        private fun setColor(primaryColor: Int, secondaryColor: Int) {
            binding?.apply {
                val backgroundColor = getColor("backgroundColor", "#FEF7FF")
                root.setBackgroundColor(backgroundColor)
                secondHand.setBackgroundColor(secondaryColor)
                minuteHand.setBackgroundColor(primaryColor)
                hourHand.setBackgroundColor(primaryColor)

                val darkerColor = ColorUtils.blendARGB(backgroundColor, 0xFF000000.toInt(), 0.3f)
                val lighterColor = ColorUtils.blendARGB(backgroundColor, 0xFFFFFFFF.toInt(), 0.3f)
                mainFrame.setShadowColorDark(darkerColor)
                mainFrame.setShadowColorLight(lighterColor)
                secondFrame.setShadowColorDark(darkerColor)
                secondFrame.setShadowColorLight(lighterColor)
            }
        }

         */

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
}
