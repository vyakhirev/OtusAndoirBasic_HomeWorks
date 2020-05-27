package com.vyakhirev.filmsinfo.di

import com.vyakhirev.filmsinfo.model.db.MovieDao
import com.vyakhirev.filmsinfo.model.db.MoviesDatabase
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, PrefsModule::class, AppModule::class])
interface ViewModelComponent {
    fun inject(viewModel: FilmListViewModel)
//    fun providesmovieDao(): MovieDao
//    fun providesRoomDatabase(): MoviesDatabase
}