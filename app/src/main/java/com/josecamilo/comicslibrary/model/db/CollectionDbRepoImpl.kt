package com.josecamilo.comicslibrary.model.db

class CollectionDbRepoImpl(
    private val characterDao: CharacterDao
) : CollectionDbRepo {
    override suspend fun getCharacters() = characterDao.getCharacters()

    override suspend fun getCharacterFromRepo(characterId: Int) =
        characterDao.getCharacter(characterId)

    override suspend fun addCharacter(character: DbCharacter) =
        characterDao.addCharacter(character)

    override suspend fun updateCharacter(character: DbCharacter) =
        characterDao.updateCharacter(character)

    override suspend fun deleteCharacter(character: DbCharacter) =
        characterDao.deleteCharacter(character)
}