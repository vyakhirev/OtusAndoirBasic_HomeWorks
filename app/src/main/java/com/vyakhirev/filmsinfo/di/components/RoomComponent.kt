package com.vyakhirev.filmsinfo.di.components

import com.vyakhirev.filmsinfo.di.modules.AppModule
import com.vyakhirev.filmsinfo.di.modules.RoomModule
import dagger.Component

@Component(modules = [RoomModule::class, AppModule::class])
interface RoomComponent {

    fun inject(room: RoomModule)
}
