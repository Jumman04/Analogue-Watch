package com.jummania.analogue_watch.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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

        binding?.apply {
            val backgroundColor = getColor("backgroundColor", "#FEF7FF")
            val primaryColor = getColor("primaryColor", "#1D1B20")
            val secondaryColor = getColor("secondaryColor", "#C90000")

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

            clock.setTextSize(preferenceManager.getInt("markerTextSize", 22).toFloat())
            clock.setVolume(preferenceManager.getInt("volume", 1) / 10f)

            if (!getBoolean("frame")) {
                mainFrame.setShadowElevation(0f)
                secondFrame.setShadowElevation(0f)
                mainFrame.setShapeAppearanceModel(NeumorphShapeAppearanceModel.builder().build())
            } else {
                val darkerColor = ColorUtils.blendARGB(backgroundColor, 0xFF000000.toInt(), 0.3f)
                val lighterColor = ColorUtils.blendARGB(backgroundColor, 0xFFFFFFFF.toInt(), 0.3f)
                mainFrame.setShadowColorDark(darkerColor)
                mainFrame.setShadowColorLight(lighterColor)
                secondFrame.setShadowColorDark(darkerColor)
                secondFrame.setShadowColorLight(lighterColor)
            }

            root.setBackgroundColor(backgroundColor)
        }

        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
                menu.clear()
                inflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                if (item.itemId == R.id.settings) parentFragmentManager.beginTransaction()
                    .setReorderingAllowed(true).addToBackStack(null)
                    .replace(R.id.fragmentContainerView, SettingsFragment()).commit()
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (activity as AppCompatActivity?)?.supportActionBar?.let {
            it.title = "Analogue Watch"
            it.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun getColor(key: String, defaultColor: String): Int {
        return preferenceManager.getInt(key, defaultColor.toColorInt())
    }

    private fun getBoolean(key: String): Boolean {
        return preferenceManager.getBoolean(key, true)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
