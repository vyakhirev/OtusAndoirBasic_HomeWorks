package com.vyakhirev.filmsinfo

import android.content.Intent
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.vyakhirev.filmsinfo.presentation.view.ListMovieFragment
import com.vyakhirev.filmsinfo.presentation.view.MainActivity
import com.vyakhirev.filmsinfo.presentation.view.adapters.FilmsAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RecyclerViewTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
            ViewActions.click().perform(uiController, view.findViewById<View>(viewId))
    }

    @Test
    fun checkRecyclerViewDisplayed() {
        onView(ViewMatchers.withId(R.id.filmsRecyclerView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkClickOnPoster() {
        var mainActivity: IntentsTestRule<MainActivity> = IntentsTestRule(
            MainActivity::class.java)
        mainActivity.launchActivity(Intent())
        launchFragmentInContainer<ListMovieFragment>()
        onView(ViewMatchers.withId(R.id.filmsRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<FilmsAdapter.FilmsViewHolder>(
                    2,
                    clickOnViewChild(R.id.posterImgView)
                )
            )

        onView(ViewMatchers.withId(R.id.detailF))
            .check(matches(isDisplayed()))
    }
}
