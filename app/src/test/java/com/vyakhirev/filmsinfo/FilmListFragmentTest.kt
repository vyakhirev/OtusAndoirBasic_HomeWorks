package com.vyakhirev.filmsinfo

import android.os.Build
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.fragment.app.testing.launchFragmentInContainer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.vyakhirev.filmsinfo.view.ListMovieFragment
import kotlinx.android.synthetic.main.fragment_detail_movie.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk =  [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class MainFragmentTest {

    @Test
    fun testEventFragment() {
        val fragmentArgs = Bundle()
        val scenario = launchFragmentInContainer<ListMovieFragment>(fragmentArgs)

        scenario.onFragment { fragment ->
            val expectedText = "Hello World!"
            val actualText = fragment.titleTV
            Assert.assertEquals(expectedText, actualText)
        }
//        Espresso.onView(withId(R.id.message))
//            .check(ViewAssertions.matches(ViewMatchers.withText("Hello World!")))
    }


}