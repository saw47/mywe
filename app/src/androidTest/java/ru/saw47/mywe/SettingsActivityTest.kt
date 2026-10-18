package ru.saw47.mywe

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.saw47.mywe.R
import org.junit.Test
import org.junit.runner.RunWith
import ui.SettingsActivity

@RunWith(AndroidJUnit4::class)
class SettingsActivityTest {

    @Test
    fun settings_showsImportAndExportTiles() {
        ActivityScenario.launch(SettingsActivity::class.java).use {
            onView(withId(R.id.import_button))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()))
                .check(matches(withText(R.string.import_button)))
            onView(withId(R.id.export_button))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()))
                .check(matches(withText(R.string.export_button)))
        }
    }
}
