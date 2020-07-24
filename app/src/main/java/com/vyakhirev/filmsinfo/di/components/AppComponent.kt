package com.vyakhirev.filmsinfo.di.components

import android.app.Application
import com.vyakhirev.filmsinfo.di.modules.ApiModule
import com.vyakhirev.filmsinfo.di.modules.AppModule
import com.vyakhirev.filmsinfo.di.modules.PrefsModule
import com.vyakhirev.filmsinfo.di.modules.RepositoryModule
import com.vyakhirev.filmsinfo.model.Repository
import com.vyakhirev.filmsinfo.util.MyWorker
import com.vyakhirev.filmsinfo.util.NotificationHelper
import com.vyakhirev.filmsinfo.view.FavoritesListFragment
import com.vyakhirev.filmsinfo.view.ListMovieFragment
import com.vyakhirev.filmsinfo.view.MainActivity
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class, ApiModule::class, PrefsModule::class])
interface AppComponent {
    fun inject(viewModel: FilmListViewModel)
    fun inject(listFragment: ListMovieFragment)
    fun inject(favoritesListFragment: FavoritesListFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(application: Application)
    fun inject(repository: Repository)
    fun inject(myWorker: MyWorker)
    fun inject(notificationHelper: NotificationHelper)
}
