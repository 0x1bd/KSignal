package io.github.kvxd.ksignal

/**
 * Applies a transformation to the emitted value.
 */
fun <T, R> Signal<T>.map(transform: (T) -> R): Signal<R> {
    val result = Signal<R>()
    connect { result.emit(transform(it)) }
    return result
}

/**
 * Emits only values that pass the given predicate.
 */
fun <T> Signal<T>.filter(predicate: (T) -> Boolean): Signal<T> {
    val result = Signal<T>()
    connect { if (predicate(it)) result.emit(it) }
    return result
}

/**
 * Emits only values that are instances of the specified type [R].
 */
inline fun <reified R> Signal<*>.filterIsInstance(): Signal<R> {
    val result = Signal<R>()
    connect { value ->
        if (value is R) result.emit(value)
    }
    return result
}

/**
 * Emits only the first [count] values.
 */
fun <T> Signal<T>.take(count: Int): Signal<T> {
    val result = Signal<T>()
    var taken = 0
    connect {
        if (taken++ < count) result.emit(it)
    }
    return result
}

/**
 * Performs an action on each emitted value.
 */
fun <T> Signal<T>.onEach(action: (T) -> Unit): Signal<T> {
    connect(action)
    return this
}

/**
 * Emits only when the value has changed.
 */
fun <T> Signal<T>.distinctUntilChanged(): Signal<T> {
    val result = Signal<T>()
    var last: T? = null
    var hasLast = false
    connect {
        if (!hasLast || it != last) {
            hasLast = true
            last = it
            result.emit(it)
        }
    }
    return result
}

/**
 * Accumulates values from the signal and emits the final accumulated result.
 */
fun <T, R> Signal<T>.fold(initial: R, operation: (R, T) -> R): Signal<R> {
    val result = Signal<R>()
    var accumulator = initial

    connect {
        accumulator = operation(accumulator, it)
        result.emit(accumulator)
    }

    return result
}

/**
 * Flattens a signal of signals into a single signal.
 */
fun <T> Signal<Signal<T>>.flatten(): Signal<T> {
    val result = Signal<T>()
    connect { inner -> inner.connect { result.emit(it) } }
    return result
}