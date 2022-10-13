package ru.skillbranch.gameofthrones.ui.houses

import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.CheckedTextView
import android.widget.SearchView
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayout
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.databinding.FragmentHousesBinding
import ru.skillbranch.gameofthrones.extensions.applyMargin
import ru.skillbranch.gameofthrones.ui.RootActivity
import kotlin.math.hypot
import kotlin.math.max

/**
 * A simple [Fragment] subclass.
 */
class HousesFragment : Fragment(R.layout.fragment_houses) {
    private val binding by viewBinding(FragmentHousesBinding::bind)
    private val args: HousesFragmentArgs by navArgs()
    private lateinit var colors: Array<Int>
    private lateinit var housesPageAdapter: HousesPageAdapter
    private var showBookmarked: Boolean = false

    @ColorInt
    private var currentColor: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        showBookmarked = args.showOnlyFavorite
        housesPageAdapter = HousesPageAdapter(childFragmentManager, showBookmarked)
        colors = requireContext().run {
            arrayOf(
                getColor(R.color.stark_primary),
                getColor(R.color.lannister_primary),
                getColor(R.color.targaryen_primary),
                getColor(R.color.baratheon_primary),
                getColor(R.color.greyjoy_primary),
                getColor(R.color.martel_primary),
                getColor(R.color.tyrel_primary)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        with(menu.findItem(R.id.action_search)?.actionView as SearchView) {
            queryHint = "Search character"
        }
        with(menu.findItem(R.id.action_favorites)?.actionView as CheckedTextView) {
            isChecked = showBookmarked

            if (isChecked) setCheckMarkDrawable(R.drawable.ic_checked_24)
            else setCheckMarkDrawable(R.drawable.ic_not_checked_24)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorites -> {
                with(item.actionView as CheckedTextView) {
                    showBookmarked = !isChecked

                    val action = HousesFragmentDirections.actionNavHousesSelf(showBookmarked)
                    findNavController().navigate(action, navOptions {
                        launchSingleTop = true
                    })
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as RootActivity).setSupportActionBar(binding.toolbar)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {

            activity?.window?.let {
                ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { view, insets ->
                    val barsInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
                    val statusBarHeight = barsInsets.top
                    view.applyMargin(top = statusBarHeight)

                    insets
                }
            }
        }
        if (currentColor != -1)
            binding.appbar.setBackgroundColor(currentColor)
        binding.viewPager.adapter = housesPageAdapter
        with(binding.tabs) {
            setupWithViewPager(binding.viewPager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {}
                override fun onTabUnselected(p0: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position ?: 0
                    val appBarColor =
                        (binding.appbar.background as? MaterialShapeDrawable)?.let {
                            it.fillColor?.defaultColor
                        } ?: (binding.appbar.background as? ColorDrawable)?.color

                    if (appBarColor != colors[position]) {
                        val rect = Rect()
                        val tabView = tab?.view as View
                        tabView.postDelayed(
                            {
                                tabView.getGlobalVisibleRect(rect)
                                animateAppbarReval(position, rect.centerX(), rect.centerY())
                            },
                            100
                        )
//                        tabView.getGlobalVisibleRect(rect)
//                        animateAppbarReval(position, rect.centerX(), rect.centerY())
                    }
                }
            })
        }
    }

    private fun animateAppbarReval(position: Int, centerX: Int, centerY: Int) {
        val endRadius = max(
            hypot(centerX.toDouble(), centerY.toDouble()),
            hypot(binding.appbar.width.toDouble() - centerX.toDouble(), centerY.toDouble())
        )
        with(binding.revealView) {
            visibility = View.VISIBLE
            setBackgroundColor(colors[position])
        }
        ViewAnimationUtils.createCircularReveal(
            binding.revealView,
            centerX,
            centerY,
            0f,
            endRadius.toFloat()
        ).apply {
            doOnEnd {
                binding.appbar.setBackgroundColor(colors[position])
                binding.revealView.visibility = View.INVISIBLE
            }
            start()
        }
        currentColor = colors[position]
    }

}
