package de.kvxd.ksignal

/**
 * Merges multiple signals into a single signal.
 */
fun <T> merge(vararg signals: Signal<T>): Signal<T> {
    val result = Signal<T>()
    signals.forEach { signal -> signal.connect { result.emit(it) } }
    return result
}

/**
 * Zips two signals together, emitting pairs only when both have emitted.
 */
fun <A, B> zip(a: Signal<A>, b: Signal<B>): Signal<Pair<A, B>> {
    val result = Signal<Pair<A, B>>()
    val queueA = mutableListOf<A>()
    val queueB = mutableListOf<B>()

    a.connect {
        queueA.add(it)
        if (queueB.isNotEmpty()) {
            result.emit(queueA.removeAt(0) to queueB.removeAt(0))
        }
    }

    b.connect {
        queueB.add(it)
        if (queueA.isNotEmpty()) {
            result.emit(queueA.removeAt(0) to queueB.removeAt(0))
        }
    }

    return result
}
