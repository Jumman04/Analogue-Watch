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
