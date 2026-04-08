package ru.skillbranch.gameofthrones.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

context(owner: LifecycleOwner)
fun <T> Flow<T>.collectLatestOnCreated(
    collector: suspend (T) -> Unit
) = collectLatestOnState(
    lifecycle = owner.lifecycle,
    state = Lifecycle.State.CREATED,
    collector = collector
)

context(fragment: Fragment)
fun <T> Flow<T>.collectOnViewCreated(
    collector: suspend (T) -> Unit
) = collectOnState(
    lifecycle = fragment.viewLifecycleOwner.lifecycle,
    state = Lifecycle.State.CREATED,
    collector = collector
)

context(fragment: Fragment)
fun <T> Flow<T>.collectLatestOnViewCreated(
    collector: suspend (T) -> Unit
) = collectLatestOnState(
    lifecycle = fragment.viewLifecycleOwner.lifecycle,
    state = Lifecycle.State.CREATED,
    collector = collector
)

context(fragment: Fragment)
fun <T> Flow<T>.collectOnViewResume(
    collector: suspend (T) -> Unit
) = fragment.viewLifecycleOwner.lifecycleScope.launch {
    fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
        collect(collector)
    }
}

context(fragment: Fragment)
fun <T> Flow<T>.collectLatestOnViewResume(
    collector: suspend (T) -> Unit
) = collectLatestOnState(
    lifecycle = fragment.viewLifecycleOwner.lifecycle,
    state = Lifecycle.State.RESUMED,
    collector = collector
)

context(owner: LifecycleOwner)
fun <T> Flow<T>.collectLatestOnResume(
    collector: suspend (T) -> Unit
) = collectLatestOnState(
    lifecycle = owner.lifecycle,
    state = Lifecycle.State.RESUMED,
    collector = collector
)

fun <T> Flow<T>.collectLatestOnResume(
    lifecycleOwner: LifecycleOwner,
    collector: suspend (T) -> Unit,
) = collectLatestOnState(
    lifecycle = lifecycleOwner.lifecycle,
    state = Lifecycle.State.RESUMED,
    collector = collector
)

fun <T> Flow<T>.collectLatestOnState(
    lifecycle: Lifecycle,
    state: Lifecycle.State,
    collector: suspend (T) -> Unit
) = lifecycle.coroutineScope.launch {
    lifecycle.repeatOnLifecycle(state) {
        collectLatest(collector)
    }
}

fun <T> Flow<T>.collectOnState(
    lifecycle: Lifecycle,
    state: Lifecycle.State,
    collector: suspend (T) -> Unit
) = lifecycle.coroutineScope.launch {
    lifecycle.repeatOnLifecycle(state) {
        collect(collector)
    }
}