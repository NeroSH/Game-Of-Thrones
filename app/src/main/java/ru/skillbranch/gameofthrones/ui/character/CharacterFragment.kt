package ru.skillbranch.gameofthrones.ui.character

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_character.*
import kotlinx.android.synthetic.main.fragment_houses.toolbar
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.HouseType
import ru.skillbranch.gameofthrones.ui.RootActivity

/**
 * A simple [Fragment] subclass.
 */
class CharacterFragment : Fragment() {
    private val args: CharacterFragmentArgs by navArgs()
    private lateinit var characterViewModel: CharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        characterViewModel =
            ViewModelProviders.of(this, CharacterViewModelFactory(args.characterId))
                .get(CharacterViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_character, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val houseType = HouseType.fromString(args.house)
        val arms = houseType.coastOfArms
        val scrim = houseType.primaryColor
        val scrimDark = houseType.darkColor

        val rootActivity = requireActivity() as RootActivity
        rootActivity.setSupportActionBar(toolbar)
        rootActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = args.title
        }
        iv_arms.setImageResource(arms)
        with(collapsing_layout) {
            setBackgroundResource(scrim)
            setContentScrimResource(scrim)
            setStatusBarScrimResource(scrimDark)
        }

        collapsing_layout.post { collapsing_layout.requestLayout() }

        characterViewModel.getCharacter().observe(viewLifecycleOwner, Observer<CharacterFull>{ character ->
            if(character == null) return@Observer

            val iconColor = requireContext().getColor(houseType.accentColor)
            listOf(tv_words_label, tv_born_label, tv_titles_label, tv_aliases_label)
                .forEach { it.compoundDrawables.first().setTint(iconColor) }

            tv_words.text = character.words
            tv_born.text = character.born
            tv_titles.text = character.titles
                .filter { it.isNotEmpty() }
                .joinToString(separator="\n")
            tv_aliases.text = character.aliases
                .filter { it.isNotEmpty() }
                .joinToString(separator="\n")

            character.father?.let{
                group_father.visibility = View.VISIBLE
                btn_father.text = it.name
                val action = CharacterFragmentDirections.actionNavCharacterSelf(it.id, it.house, it.name)
                btn_father.setOnClickListener { findNavController().navigate(action) }
            }
            //Log.d("character", "characterInfo: $character")
            character.mother?.let{
                group_mother.visibility = View.VISIBLE
                btn_mother.text = it.name
                val action = CharacterFragmentDirections.actionNavCharacterSelf(it.id, it.house, it.name)
                btn_mother.setOnClickListener { findNavController().navigate(action) }
            }
            if(character.died.isNotBlank()){
                Snackbar.make(coordinator,"Died in ${character.died}",Snackbar.LENGTH_INDEFINITE)
                    .show()
            }
        })

    }


}
