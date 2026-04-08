package ru.skillbranch.gameofthrones.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.RelativeCharacter

@Dao
interface CharacterDao : BaseDao<Character> {
    @Query("SELECT * FROM CharacterItem WHERE house = :title")
    fun findCharacters(title: String): LiveData<List<CharacterItem>>

    @Query("SELECT * FROM CharacterItem WHERE house = :title")
    fun findCharacterList(title: String): List<CharacterItem>

    @Query("SELECT * FROM characters WHERE id = :characterId")
    fun findCharacter(characterId: String): Flow<Character>

    @Query("SELECT * FROM characters WHERE id = :characterId")
    fun findCharacterFull(characterId: String): Character?

    @Query("SELECT * FROM characters WHERE id = :characterId")
    fun findCharacterObject(characterId: String): Character?

    @Query("SELECT id, name, house_id, is_bookmarked " +
            " FROM characters WHERE id = :characterId")
    fun findRelativeCharacter(characterId: String): RelativeCharacter?

    //    fun findCharacterLiveData(characterId: String) : LiveData<Character>
    @Query("DELETE FROM characters")
    fun deleteTable()

    @Transaction
    fun upsert(objList: List<Character>) {
        insert(objList)
            .mapIndexed { index, l -> if (l == -1L) objList[index] else null }
            .filterNotNull()
            .also { if (it.isNotEmpty()) update(it) }
    }

    @Update
    fun updateCharacter(character: Character)

    @Query("UPDATE characters SET is_bookmarked = :state WHERE id = :characterId")
    fun setBookmarked(characterId: String, state: Boolean)
}