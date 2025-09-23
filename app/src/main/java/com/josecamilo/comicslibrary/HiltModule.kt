package com.josecamilo.comicslibrary

import android.content.Context
import androidx.room.Room
import com.josecamilo.comicslibrary.model.api.ApiService
import com.josecamilo.comicslibrary.model.api.MarvelApiRepo
import com.josecamilo.comicslibrary.model.db.CharacterDao
import com.josecamilo.comicslibrary.model.db.CollectionDb
import com.josecamilo.comicslibrary.model.db.CollectionDbRepo
import com.josecamilo.comicslibrary.model.db.CollectionDbRepoImpl
import com.josecamilo.comicslibrary.model.db.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    fun provideApiRepo() = MarvelApiRepo(ApiService.api)

    @Provides
    fun provideCollectionDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            CollectionDb::class.java,
            Constants.DB
        ).build()

    @Provides
    fun provideCharacterDao(db: CollectionDb) = db.characterDao()

    @Provides
    fun providesDbRepoImpl(characterDao: CharacterDao) : CollectionDbRepo =
        CollectionDbRepoImpl(characterDao)
}