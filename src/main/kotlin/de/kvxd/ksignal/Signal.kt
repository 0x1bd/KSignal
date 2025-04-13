package de.kvxd.ksignal

/**
 * A lightweight observable signal system.
 * Listeners can be connected to receive events.
 */
class Signal<T> {

    private val listeners = mutableListOf<Listener<T>>()

    /**
     * Connects a listener to this signal.
     * @return Listener handle for disconnecting.
     */
    fun connect(listener: (T) -> Unit): Listener<T> {
        val wrapper = Listener(listener)
        listeners += wrapper
        return wrapper
    }

    /**
     * Emits an event to all connected listeners.
     */
    fun emit(value: T) {
        val iterator = listeners.iterator()
        while (iterator.hasNext()) {
            val listener = iterator.next()
            listener.call(value)
            if (listener.once) iterator.remove()
        }
    }

    /**
     * Represents a connection to a listener.
     */
    class Listener<T>(
        private val listener: ((T) -> Unit)? = null,
        internal val once: Boolean = false
    ) {
        fun call(value: T) {
            listener?.invoke(value)
        }

        fun disconnect(signal: Signal<T>) {
            signal.listeners.remove(this)
        }
    }

    /**
     * Connects a listener that is only called once.
     */
    fun connectOnce(listener: (T) -> Unit): Listener<T> {
        val wrapper = Listener(listener, once = true)
        listeners += wrapper
        return wrapper
    }

    /**
     * Removes all listeners
     */
    fun clear() {
        listeners.clear()
    }
}
