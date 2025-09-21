package com.josecamilo.comicslibrary

import com.josecamilo.comicslibrary.model.api.ApiService
import com.josecamilo.comicslibrary.model.api.MarvelApiRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    fun provideApiRepo() = MarvelApiRepo(ApiService.api)
}