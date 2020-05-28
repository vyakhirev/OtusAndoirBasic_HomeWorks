package com.vyakhirev.filmsinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vyakhirev.filmsinfo.di.AppModule
import com.vyakhirev.filmsinfo.di.DaggerViewModelComponent
import com.vyakhirev.filmsinfo.model.Movie
import com.vyakhirev.filmsinfo.model.MovieResponse
import com.vyakhirev.filmsinfo.model.db.MovieDao
import com.vyakhirev.filmsinfo.model.network.MovieApiClient
import com.vyakhirev.filmsinfo.util.SharedPreferencesHelper
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class FilmListViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

//    @Mock
//    lateinit var movieClient: MovieApiClient

    @Mock
    lateinit var prefs: SharedPreferencesHelper

//    val dao = Mockito.mock(MovieDao::class.java)

    val application = Mockito.mock(App::class.java)


    var stLoc=Mockito.mock(FilmListViewModel::class.java)
    var movieClient = Mockito.mock(MovieApiClient::class.java)
    var listViewModel = FilmListViewModel(movieClient, true)

    private val key = "Test key"

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
    fun getMovieSuccess() {
        Mockito.`when`(prefs.getCacheDuration()).thenReturn("10")
        val movie = Movie(1, "test", "testtest")
        val movieslist = listOf(movie)
        val movieResponce = MovieResponse(1, movieslist, 20, 1, null, "")
//        val testSingle=Single.just(movieResponce)
        val testSingle = Single.just(movieResponce)
//        val testCompletable=Completable.complete()
        Mockito.`when`(movieClient.getPopular(BuildConfig.TMDB_API_KEY, "ru", 1))
            .thenReturn(testSingle)
//        Mockito.`when`(stLoc.storeLocally(movieslist)).thenReturn(testCompletable)
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
//        Assert.assertEquals(null, listViewModel.onMessageError.value)
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
//    @Test
//    fun getAnimalsSuccess() {
//        Mockito.`when`(prefs.getCacheDuration())
//        val animal = Animal("cow", null, null, null, null, null, null)
//        val animalList = listOf(animal)
//
//        val testSingle = Single.just(animalList)
//
//        Mockito.`when`(movieClient.getAnimals(key)).thenReturn(testSingle)
//
//        listViewModel.refresh()
//
//        Assert.assertEquals(1, listViewModel.animals.value?.size)
//        Assert.assertEquals(false, listViewModel.loadError.value)
//        Assert.assertEquals(false, listViewModel.loading.value)
//    }
//
//    @Test
//    fun getAnimalsFailure() {
//        Mockito.`when`(prefs.getApiKey()).thenReturn(key)
//        val testSingle = Single.error<List<Animal>>(Throwable())
//        val keySingle = Single.just(ApiKey("OK", key))
//
//        Mockito.`when`(movieClient.getAnimals(key)).thenReturn(testSingle)
//        Mockito.`when`(movieClient.getApiKey()).thenReturn(keySingle)
//
//        listViewModel.refresh()
//
//        Assert.assertEquals(null, listViewModel.animals.value)
//        Assert.assertEquals(false, listViewModel.loading.value)
//        Assert.assertEquals(true, listViewModel.loadError.value)
//    }
//
//    @Test
//    fun getKeySuccess() {
//        Mockito.`when`(prefs.getApiKey()).thenReturn(null)
//        val apiKey = ApiKey("OK", key)
//        val keySingle = Single.just(apiKey)
//
//        Mockito.`when`(movieClient.getApiKey()).thenReturn(keySingle)
//
//        val animal = Animal("cow", null, null, null, null, null, null)
//        val animalsList = listOf(animal)
//
//        val testSingle = Single.just(animalsList)
//        Mockito.`when`(movieClient.getAnimals(key)).thenReturn(testSingle)
//
//        listViewModel.refresh()
//
//        Assert.assertEquals(1, listViewModel.animals.value?.size)
//        Assert.assertEquals(false, listViewModel.loadError.value)
//        Assert.assertEquals(false, listViewModel.loading.value)
//    }
//
//    @Test
//    fun getKeyFailure() {
//        Mockito.`when`(prefs.getApiKey()).thenReturn(null)
//        val keySingle = Single.error<ApiKey>(Throwable())
//
//        Mockito.`when`(movieClient.getApiKey()).thenReturn(keySingle)
//
//        listViewModel.refresh()
//
//        Assert.assertEquals(null, listViewModel.animals.value)
//        Assert.assertEquals(false, listViewModel.loading.value)
//        Assert.assertEquals(true, listViewModel.loadError.value)
//    }