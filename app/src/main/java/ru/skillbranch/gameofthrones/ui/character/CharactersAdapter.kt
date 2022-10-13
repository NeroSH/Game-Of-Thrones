package ru.skillbranch.gameofthrones.ui.character

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.databinding.ItemCharacterBinding

class CharactersAdapter(private val listener: (CharacterItem) -> Unit) :
    ListAdapter<CharacterItem, CharactersAdapter.CharacterViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val container =
            LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(container)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding by viewBinding(ItemCharacterBinding::bind)
        fun bind(character: CharacterItem, listener: (CharacterItem) -> Unit) {
            with(itemView) {
                binding.textViewName.text =
                    if (character.name.isBlank()) "Information is unknown" else character.name
                val listOfAliases =
                    character.titles.plus(character.aliases).filter { it.isNotBlank() }
                binding.textViewAliases.text = if (listOfAliases.isEmpty()) "Information is unknown"
                else listOfAliases.joinToString(",")
                binding.imageAvatar.setImageResource(character.house.icon)
                setOnClickListener { listener(character) }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CharacterItem>() {
        override fun areItemsTheSame(oldItem: CharacterItem, newItem: CharacterItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CharacterItem, newItem: CharacterItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

}