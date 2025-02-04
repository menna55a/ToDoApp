package com.route.todoapp.ui.home.fragments.settings_fragment

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.route.todoapp.R
import com.route.todoapp.databinding.FragmentSettingsBinding
import java.util.*

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val languages = listOf("English", "Arabic")
        val languageAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, languages)
        binding.autoCompleteTVLanguages.setAdapter(languageAdapter)

        val modes = listOf("Light", "Dark")
        val modeAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, modes)
        binding.autoCompleteTVModes.setAdapter(modeAdapter)

        val savedLanguage = getSavedLanguage()
        binding.autoCompleteTVLanguages.setText(savedLanguage, false)

        val savedMode = getSavedMode()
        binding.autoCompleteTVModes.setText(savedMode, false)

        binding.autoCompleteTVLanguages.setOnItemClickListener { _, _, position, _ ->
            val selectedLanguage = languages[position]
            changeLanguage(selectedLanguage)
        }

        binding.autoCompleteTVModes.setOnItemClickListener { _, _, position, _ ->
            val selectedMode = modes[position]
            changeTheme(selectedMode)
        }
    }

    private fun changeLanguage(language: String) {
        val locale = when (language) {
            "Arabic" -> Locale("ar")
            else -> Locale("en")
        }
        saveLanguage(language)
        updateLocale(locale)
    }

    private fun updateLocale(locale: Locale) {
        Locale.setDefault(locale)
        val resources: Resources = resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        requireActivity().recreate()
    }

    private fun changeTheme(mode: String) {
        when (mode) {
            "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        saveMode(mode)

        requireActivity().recreate()
    }

    private fun saveLanguage(language: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("App_Lang", language).apply()
    }

    private fun getSavedLanguage(): String {
        val sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        return sharedPreferences.getString("App_Lang", "English") ?: "English"
    }

    private fun saveMode(mode: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("App_Mode", mode).apply()
    }

    private fun getSavedMode(): String {
        val sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        return sharedPreferences.getString("App_Mode", "Light") ?: "Light"
    }
}
