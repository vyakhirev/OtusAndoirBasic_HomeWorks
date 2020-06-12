package com.vyakhirev.filmsinfo

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import com.vyakhirev.filmsinfo.view.ListMovieFragment
import kotlinx.android.synthetic.main.fragment_detail_movie.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class FilmListFragmentTest {

    @Test
    fun testEventFragment() {
        val fragmentArgs = Bundle()
        val scenario = launchFragmentInContainer<ListMovieFragment>(fragmentArgs)

        scenario.onFragment { fragment ->
            val expectedText = "Hello World!"
            val actualText = fragment.titleTV
            Assert.assertEquals(expectedText, actualText)
        }
    }
}
