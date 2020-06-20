package com.vyakhirev.filmsinfo.di

import com.vyakhirev.filmsinfo.presentation.viewmodel.FilmListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, PrefsModule::class, AppModule::class,RoomModule::class])
interface ViewModelComponent {
    fun inject(viewModel: FilmListViewModel)
}
