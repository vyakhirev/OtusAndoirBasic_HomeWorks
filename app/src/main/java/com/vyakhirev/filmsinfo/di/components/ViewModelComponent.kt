package com.vyakhirev.filmsinfo.di.components

import com.vyakhirev.filmsinfo.di.modules.RepositoryModule
import com.vyakhirev.filmsinfo.viewmodel.FilmListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class])
interface ViewModelComponent {
    fun inject(viewModel: FilmListViewModel)
}
