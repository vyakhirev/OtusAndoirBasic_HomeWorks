package com.vyakhirev.filmsinfo

import android.os.Build
import androidx.fragment.app.testing.launchFragmentInContainer
import com.vyakhirev.filmsinfo.view.DetailMovieFragment
import com.vyakhirev.filmsinfo.view.MainActivity
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class MainFragmentTest {

    @Test
    fun testDetailsFragment() {
        val activity: MainActivity = Robolectric.buildActivity<MainActivity>(MainActivity::class.java)
            .setup()
            .get()
        activity.supportFragmentManager.fragments.add(DetailMovieFragment())
        val scenario = launchFragmentInContainer<DetailMovieFragment>()

        scenario.onFragment { fragment ->
            val expectedText = "Ad Astra"
            val actualText = fragment.getText(R.id.titleTV)

            Assert.assertEquals(expectedText, actualText)
        }
    }
}
