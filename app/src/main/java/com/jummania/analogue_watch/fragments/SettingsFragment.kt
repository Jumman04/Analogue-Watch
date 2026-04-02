package com.jummania.analogue_watch.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jummania.analogue_watch.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val version = preferenceScreen.findPreference<Preference>("version")
        version?.title = "Version: ${getString(R.string.versionName)}"

        (activity as AppCompatActivity?)?.supportActionBar?.let {
            it.title = "Settings"
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "theme" -> {
                val themes = resources.getStringArray(R.array.themes)
                var position = themes.indexOf(
                    getSharedPreferences()?.getString(
                        "theme", "System default"
                    )
                )
                MaterialAlertDialogBuilder(requireContext()).setTitle("Choose theme")
                    .setPositiveButton("Ok") { _, _ ->
                        val theme = themes[position]
                        changeTheme(theme)
                        getSharedPreferences()?.edit {
                            putString("theme", theme)
                        }
                        preference.summary = theme
                    }.setNegativeButton("Cancel") { _, _ ->

                    }.setSingleChoiceItems(
                        themes, position
                    ) { _, which ->
                        position = which
                    }.show()
            }

            "clear" -> {
                getSharedPreferences()?.edit {
                    clear()
                }
                activity?.recreate()
                showMessage("All Settings have been reset.")
            }


            "share" -> startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(
                        Intent.EXTRA_TEXT,
                        "বাংলাদেশী কবিতা প্রেমীদের জন্যে।\nএকসাথে একটি প্লাটফর্মে, সকল ক্ষ্যাতিমান কবিদের কবিতা শুনতে, আবৃতি করতে চাইলে এই অ্যাপসটি ডাউনলোড করুন।\n\nApp Link: https://play.google.com/store/apps/details?id=${requireActivity().packageName}"
                    ), "Share this App"
                )
            )

            "website" -> browseInternet("https://apps.jummania.com")
            "more" -> browseInternet("https://play.google.com/store/apps/developer?id=Mamomi+Soft+Heart")
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun browseInternet(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        } catch (_: Exception) {
            showMessage("No app found to handle this request.")
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun changeTheme(theme: String) {
        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                "Light" -> AppCompatDelegate.MODE_NIGHT_NO
                "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }

    private fun getSharedPreferences(): SharedPreferences? {
        return preferenceManager?.sharedPreferences
    }
}