package com.vyakhirev.filmsinfo

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vyakhirev.filmsinfo.view.FavoritesListFragment
import com.vyakhirev.filmsinfo.view.ListMovieFragment
import com.vyakhirev.filmsinfo.view.adapters.FilmsAdapter
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
        val scenario = launchFragmentInContainer<ListMovieFragment>()
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
        val scenarioFavor = launchFragmentInContainer<FavoritesListFragment>()

        scenarioFavor.onFragment {
            val expectedText = "Побег из Шоушенка"
            val actualText = it.favTitleTV.text.toString()

            Assert.assertEquals(expectedText, actualText)
        }
    }

//    @Test
//    fun openDetailTest(){
//        val scenario = launchFragmentInContainer<ListMovieFragment>()
//        Espresso.onView(withId(R.id.filmsRecyclerView))
//            .perform(
//                RecyclerViewActions.actionOnItemAtPosition<FilmsAdapter.FilmsViewHolder>(
//                    2,
//                    clickOnViewChild(R.id.titleTV)
//                )
//            )
//
//    }
}
