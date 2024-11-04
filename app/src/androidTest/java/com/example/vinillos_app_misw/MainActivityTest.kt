package com.example.vinillos_app_misw

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.vinillos_app_misw.presentation.ui.views.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun IniciarSesionComoUsuarioNormalVerListaAlbumesYDetalleAlbumes() {
        onView(withId(R.id.main_activity))
            .check(matches(isDisplayed()))

        onView(withId(R.id.user_avatar))
            .perform(click())


        onView(withId(R.id.home_activity))
            .check(matches(isDisplayed()))

        Thread.sleep(500)

        onView(withText("Buscando América")).perform(click())

        activityRule.scenario.close()
    }
}