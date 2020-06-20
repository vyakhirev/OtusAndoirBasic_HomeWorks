package com.vyakhirev.filmsinfo

import android.content.Intent
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vyakhirev.filmsinfo.presentation.view.FavoritesListFragment
import com.vyakhirev.filmsinfo.presentation.view.MainActivity
import com.vyakhirev.filmsinfo.presentation.view.adapters.FilmsAdapter
import kotlinx.android.synthetic.main.favorite_item.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavorClickTest {

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
            click().perform(uiController, view.findViewById<View>(viewId))
    }

    @Test
    fun testFavoritesAdd() {
        var mainActivity: IntentsTestRule<MainActivity> = IntentsTestRule(
            MainActivity::class.java)
        mainActivity.launchActivity(Intent())
        Espresso.onView(withId(R.id.filmsRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<FilmsAdapter.FilmsViewHolder>(
                    1,
                    clickOnViewChild(R.id.favoritesImgView)
                )
            )
    }

    @Test
    fun testEqualInFavor() {

        var mainActivity: IntentsTestRule<MainActivity> = IntentsTestRule(
            MainActivity::class.java)
        mainActivity.launchActivity(Intent())

        val scenarioFavor = launchFragmentInContainer<FavoritesListFragment>()

        scenarioFavor.onFragment {
            val expectedText = "Побег из Шоушенка"
            val actualText = it.favTitleTV.text.toString()

            Assert.assertEquals(expectedText, actualText)
        }
    }
}
