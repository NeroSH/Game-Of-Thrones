package ru.skillbranch.gameofthrones.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.Character

@Dao
interface CharacterDao : BaseDao<Character> {
    @Query("SELECT * FROM CharacterItem WHERE house = :title")
    fun findCharacters(title: String): LiveData<List<CharacterItem>>

    @Query("SELECT * FROM CharacterItem WHERE house = :title")
    fun findCharacterList(title: String): List<CharacterItem>

    @Query("SELECT * FROM CharacterFull WHERE id = :characterId")
    fun findCharacter(characterId: String) : LiveData<CharacterFull>

    @Query("SELECT * FROM CharacterFull WHERE id = :characterId")
    fun findCharacterFull(characterId: String) : CharacterFull

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
}