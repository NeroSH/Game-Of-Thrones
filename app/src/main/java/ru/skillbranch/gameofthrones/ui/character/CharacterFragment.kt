package ru.skillbranch.gameofthrones.ui.character

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.HouseType
import ru.skillbranch.gameofthrones.databinding.FragmentCharacterBinding
import ru.skillbranch.gameofthrones.ui.RootActivity

/**
 * A simple [Fragment] subclass.
 */
class CharacterFragment : Fragment(R.layout.fragment_character) {
    private val binding by viewBinding(FragmentCharacterBinding::bind)
    private val args: CharacterFragmentArgs by navArgs()
    private val characterViewModel: CharacterViewModel by viewModels {
        CharacterViewModelFactory(
            args.characterId
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_character, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.action_add_favorite)
        toggleIcon(item = item, state = args.isBookmarked)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_add_favorite -> {
                val state = args.isBookmarked
                val toast: Toast
                if (state) {
                    toast = Toast.makeText(
                        requireActivity(), "Deleted",
                        Toast.LENGTH_SHORT
                    )
                } else {
                    toast = Toast.makeText(
                        requireActivity(), "Added",
                        Toast.LENGTH_SHORT
                    )
                }
                toast.show()

                characterViewModel.setBookmarked()
                val action = CharacterFragmentDirections.actionNavCharacterSelf(
                    args.characterId,
                    args.house,
                    args.title,
                    !state
                )

                findNavController().navigate(action, navOptions {
                    launchSingleTop = true
                })
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val houseType = HouseType.fromString(args.house)
        val arms = houseType.coastOfArms
        val scrim = houseType.primaryColor
        val scrimDark = houseType.darkColor

        val rootActivity = requireActivity() as RootActivity
        rootActivity.setSupportActionBar(binding.toolbar)
        rootActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = args.title
        }
        binding.ivArms.setImageResource(arms)
        with(binding.collapsingLayout) {
            setBackgroundResource(scrim)
            setContentScrimResource(scrim)
            setStatusBarScrimResource(scrimDark)
        }

        binding.collapsingLayout.post { binding.collapsingLayout.requestLayout() }

        characterViewModel.getCharacter()
            .observe(viewLifecycleOwner, Observer<CharacterFull> { character ->
                if (character == null) return@Observer
                with(binding) {

                    val iconColor = requireContext().getColor(houseType.accentColor)
                    listOf(
                        tvWordsLabel,
                        tvBornLabel,
                        tvTitlesLabel,
                        tvAliasesLabel
                    )
                        .forEach { it.compoundDrawables.first().setTint(iconColor) }

                    tvWords.text = character.words
                    tvBorn.text = character.born
                    tvTitles.text = character.titles
                        .filter { it.isNotEmpty() }
                        .joinToString(separator = "\n")
                    tvAliases.text = character.aliases
                        .filter { it.isNotEmpty() }
                        .joinToString(separator = "\n")

                    character.father?.let {
                        groupFather.visibility = View.VISIBLE
                        btnFather.text = it.name
                        val action =
                            CharacterFragmentDirections.actionNavCharacterSelf(
                                it.id,
                                it.house,
                                it.name,
                                it.is_bookmarked
                            )
                        btnFather.setOnClickListener { findNavController().navigate(action) }
                    }
                    character.mother?.let {
                        groupMother.visibility = View.VISIBLE
                        btnMother.text = it.name
                        val action =
                            CharacterFragmentDirections.actionNavCharacterSelf(
                                it.id,
                                it.house,
                                it.name,
                                it.is_bookmarked
                            )
                        btnMother.setOnClickListener { findNavController().navigate(action) }
                    }
                    if (character.died.isNotBlank()) {
                        Snackbar.make(
                            coordinator,
                            "Died in ${character.died}",
                            Snackbar.LENGTH_INDEFINITE
                        ).show()
                    }
                }
            })
    }

    private fun toggleIcon(state: Boolean, item: MenuItem) {
        item.setIcon(if (state) R.drawable.ic_bookmarked_24 else R.drawable.ic_not_bookmarked_24)
    }
}
