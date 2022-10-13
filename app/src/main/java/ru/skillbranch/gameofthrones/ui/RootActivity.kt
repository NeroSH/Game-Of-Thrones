package ru.skillbranch.gameofthrones.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.databinding.ActivityRootBinding
import ru.skillbranch.gameofthrones.ui.slpash.SplashFragmentDirections

class RootActivity : AppCompatActivity() {

    private val rootViewModel: RootViewModel by viewModels()
    lateinit var navController: NavController
    private val binding by viewBinding(ActivityRootBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_root)
        savedInstanceState ?: prepareData()
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun prepareData() {
        rootViewModel.syncDataIfNeed().observe(this) {
            when (it) {
                is LoadResult.Loading -> {
                    navController.navigate(R.id.nav_splash)
                    //Log.d("Navigation","to nav_splash")
                }
                is LoadResult.Success -> {
                    val action = SplashFragmentDirections.actionNavSplashToNavHouses()
                    navController.navigate(action)
                }
                is LoadResult.Error -> {
                    Snackbar.make(
                        binding.rootContainer,
                        it.errorMessage.toString(),
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }
            }
        }
    }
}
