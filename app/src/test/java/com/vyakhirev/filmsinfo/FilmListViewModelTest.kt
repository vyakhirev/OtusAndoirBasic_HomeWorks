package com.vyakhirev.filmsinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vyakhirev.filmsinfo.di.AppModule
import com.vyakhirev.filmsinfo.di.DaggerViewModelComponent
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.MovieResponse
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import java.util.concurrent.Executor
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FilmListViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var prefs: SharedPreferencesHelper

    private val application = Mockito.mock(App::class.java)

    private var movieClient = Mockito.mock(MovieApiClient::class.java)
    var listViewModel = FilmListViewModel(movieClient, true)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        DaggerViewModelComponent.builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTest(movieClient))
            .prefsModule(PrefsModuleTest(prefs))
            .build()
            .inject(listViewModel)
    }

    @Test
    fun getCacheDurationSuccess() {
        Mockito.`when`(prefs.getCacheDuration()).thenReturn("10")
        val cacheDur = prefs.getCacheDuration()?.toInt()
        Assert.assertEquals(10, cacheDur)
    }

    @Test
    fun isViewLoading() {
        Mockito.`when`(prefs.getCacheDuration()).thenReturn("10")
        val testSingle = Single.error<MovieResponse>(Throwable())
        Mockito.`when`(movieClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", 1))
            .thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(false, listViewModel.isViewLoading.value)
    }

    @Test
    fun getMovieSuccess() {
        Mockito.`when`(prefs.getCacheDuration()).thenReturn("10")
        val movie = Movie(1, "test", "testtest")
        val movieslist = listOf(movie)
        val movieResponce = MovieResponse(1, movieslist, 20, 1, null, "")
        val testSingle = Single.just(movieResponce)
        Mockito.`when`(movieClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", 1))
            .thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(1, listViewModel.movies.value?.size)
        Assert.assertEquals(false, listViewModel.isViewLoading.value)
        Assert.assertEquals(null, listViewModel.onMessageError.value)
    }

    @Test
    fun getMovieError() {
        Mockito.`when`(prefs.getCacheDuration()).thenReturn("10")
        val testSingle = Single.error<MovieResponse>(Throwable())
        Mockito.`when`(movieClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", 1))
            .thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(null, listViewModel.movies.value?.size)
        Assert.assertEquals(false, listViewModel.isViewLoading.value)
    }

    @Test
    fun isMovieSizeEqual20() {
        Mockito.`when`(prefs.getCacheDuration()).thenReturn("10")
        val movie = Movie(1, "test", "testtest")
        val movieslist = listOf(movie)
        val movieResponce = MovieResponse(1, movieslist, 20, 1, null, "")
        val testSingle = Single.just(movieResponce)
        Mockito.`when`(movieClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", 1))
            .thenReturn(testSingle)
        Assert.assertEquals(20, movieResponce.totalResults)
    }

    @Before
    fun setupRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }
        }

        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }
}
