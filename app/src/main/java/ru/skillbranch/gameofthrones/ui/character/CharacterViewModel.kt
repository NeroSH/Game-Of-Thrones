package ru.skillbranch.gameofthrones.ui.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.repositories.RootRepository

class CharacterViewModel(private val characterId: String) : ViewModel() {
    private val repository = RootRepository

    //    fun getCharacter() : LiveData<CharacterFull> = repository.getCharacter(characterId)
    fun getCharacter(): LiveData<CharacterFull> {
        return repository.getCharacter(characterId)
    }

}

class CharacterViewModelFactory(private val characterId: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
            return CharacterViewModel(characterId) as T
        }
        throw  IllegalArgumentException("unknown ViewModel class")
    }
}