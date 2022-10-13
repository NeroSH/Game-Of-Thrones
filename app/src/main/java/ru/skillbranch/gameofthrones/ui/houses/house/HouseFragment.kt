package ru.skillbranch.gameofthrones.ui.houses.house

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.HouseType
import ru.skillbranch.gameofthrones.databinding.FragmentHouseBinding
import ru.skillbranch.gameofthrones.ui.character.CharactersAdapter
import ru.skillbranch.gameofthrones.ui.houses.HousesFragmentDirections

/**
 * A simple [Fragment] subclass.
 */
class HouseFragment : Fragment(R.layout.fragment_house) {
    private val binding by viewBinding(FragmentHouseBinding::bind)
    private val houseViewModel: HouseViewModel by viewModels { HouseViewModelFactory(houseName) }

    private lateinit var charactersAdapter: CharactersAdapter

    private var houseName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        houseName = arguments?.getString(HOUSE_NAME) ?: HouseType.STARK.title
        val showBookmarked = arguments?.getBoolean(ONLY_FAVORITE) ?: false

        charactersAdapter = CharactersAdapter {
            val action = HousesFragmentDirections.actionNavHousesToNavCharacter(
                it.id,
                it.house.title,
                it.name,
                it.isBookmarked
            )
            findNavController().navigate(action)
        }
        if (showBookmarked) {
            houseViewModel.getFavoriteCharacters().observe(this) {
                charactersAdapter.submitList(it)
            }
        }
        else {
            houseViewModel.getCharacters().observe(this) {
                charactersAdapter.submitList(it)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        with(menu.findItem(R.id.action_search).actionView as SearchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    houseViewModel.handleSearchQuery(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    houseViewModel.handleSearchQuery(newText)
                    return true
                }
            })
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorites -> {

            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerCharacters.apply {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        binding.recyclerCharacters.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    companion object {
        private const val HOUSE_NAME = "house_name"
        private const val ONLY_FAVORITE = "show_only_favorite"

        fun newInstance(houseName: String, showBookmarked: Boolean): HouseFragment {
            return HouseFragment().apply {
                arguments = bundleOf(
                    HOUSE_NAME to houseName,
                    ONLY_FAVORITE to showBookmarked
                )
            }
        }
    }
}
