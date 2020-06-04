package com.vyakhirev.filmsinfo

// @Config(sdk = [Build.VERSION_CODES.O_MR1])
// @RunWith(RobolectricTestRunner::class)
// class MainFragmentTest {
//    @Before
//    fun setUp() {
//        val app = getApplicationContext<LocationTrackerApplication>()
//        app.setLocationProvider(mockLocationProvider)
//    }
//    @Test
//    fun testDetailsFragment() {
//        val activity: MainActivity = Robolectric.buildActivity<MainActivity>(MainActivity::class.java)
//            .setup()
//            .get()
//        activity.supportFragmentManager.fragments.add(DetailMovieFragment())
//        val scenario = launchFragmentInContainer<DetailMovieFragment>()
//
//        scenario.onFragment { fragment ->
//            val expectedText = "Ad Astra"
//            val actualText = fragment.getText(R.id.titleTV)
//
//            Assert.assertEquals(expectedText, actualText)
//        }
//    }
// }
