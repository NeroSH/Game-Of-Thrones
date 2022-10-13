package ru.skillbranch.gameofthrones.ui.slpash

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.databinding.FragmentSplashBinding

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private lateinit var draw: Drawable
    private val binding by viewBinding(FragmentSplashBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        draw = binding.imageAnimatedDragon.drawable
        if (draw is AnimatedVectorDrawableCompat)
            (draw as AnimatedVectorDrawableCompat).start()
        else if (draw is AnimatedVectorDrawable)
            (draw as AnimatedVectorDrawable).start()
    }

    override fun onPause() {
       super.onPause()
        (draw as AnimatedVectorDrawable).stop()
    }
}
