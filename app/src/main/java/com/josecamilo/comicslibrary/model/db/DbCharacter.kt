package com.josecamilo.comicslibrary.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.josecamilo.comicslibrary.model.CharacterResult

@Entity(tableName = Constants.CHARACTER_TABLE)
data class DbCharacter(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val apiId: Int?,
    val name: String?,
    val thumbnail: String?,
    val comics: String?,
    val description: String?,
) {
    companion object {
        fun fromCharacter(character: CharacterResult) = DbCharacter(
            id = 0,
            apiId = character.id,
            name = character.name,
            thumbnail = character.thumbnail?.let { "${it.path}.${it.extension}" },
            comics = character.comics?.items?.joinToString(", ") { it.name ?: ", " } ?: "no comics",
            description = character.description
        )
    }
}