package ru.skillbranch.gameofthrones.extensions

import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

val defaultSafeAreaRect = Rect(0, 0, 0, 0)

fun WindowInsetsCompat.getSafeAreaRect(): Rect {
    val statusBarHeight = getInsets(WindowInsetsCompat.Type.statusBars())
        .top.takeIf { it != 0 }
    val navBarInsets = getInsets(WindowInsetsCompat.Type.navigationBars())
    val navBarHeight = navBarInsets.bottom.takeIf { it != 0 }
    val navBarWidth = maxOf(navBarInsets.left, navBarInsets.right)

    val cutout = displayCutout?.let {
        Rect(
            /* left = */ it.safeInsetLeft,
            /* top = */ it.safeInsetTop,
            /* right = */ it.safeInsetRight,
            /* bottom = */ it.safeInsetBottom,
        )
    } ?: defaultSafeAreaRect

    val safeLeft = maxOf(cutout.left, navBarWidth)
    val safeRight = maxOf(cutout.right, navBarWidth)
    val safeTop = statusBarHeight ?: cutout.top
    val safeBottom = navBarHeight ?: cutout.bottom

    return Rect(
        /* left = */ safeLeft,
        /* top = */ safeTop,
        /* right = */ safeRight,
        /* bottom = */ safeBottom,
    )
}

inline fun View.setSafeAreaInsetsListener(
    crossinline onNewSafeAreaReceived: (newSafeInsetsRect: Rect) -> Unit
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        onNewSafeAreaReceived(insets.getSafeAreaRect())
        insets
    }

    ViewCompat.requestApplyInsets(this)
}