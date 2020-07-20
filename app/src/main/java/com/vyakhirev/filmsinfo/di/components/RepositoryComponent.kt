package com.vyakhirev.filmsinfo.di.components

import com.vyakhirev.filmsinfo.di.modules.ApiModule
import com.vyakhirev.filmsinfo.di.modules.PrefsModule
import com.vyakhirev.filmsinfo.di.modules.RoomModule
import com.vyakhirev.filmsinfo.model.Repository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, RoomModule::class, PrefsModule::class])
interface RepositoryComponent {
    fun inject(repository: Repository)
}
