package com.josecamilo.comicslibrary.model.db

import kotlinx.coroutines.flow.Flow

interface CollectionsDbRepo {
    suspend fun getCharacters(): Flow<List<DbCharacter>>

    suspend fun getCharacterFromRepo(characterId: Int): Flow<DbCharacter>

    suspend fun addCharacter(character: DbCharacter)

    suspend fun updateCharacter(character: DbCharacter)

    suspend fun deleteCharacter(character: DbCharacter)
}