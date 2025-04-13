# KSignal
A simple, lightweight and fully extensible signal system for Kotlin applications and libraries.
Use it for clean event dispatching, reactive patters and easy-to-understand async communication without complex event buses or observers.

## Features
- 📦 Zero dependencies
- 🧩 Functional extensions (map, filter, merge, etc.)
- 🧪 Unit tested
- ✍️ Fully documented
- ⚡ Lightweight and fast

## Quick start
### Create a signal
```kotlin
val signal = Signal<String>()
```

### Connect a listener
```kotlin
signal.connect { data ->
    println("Data received: $data")
}
```

### Emit an event
```kotlin
signal.emit("Hello, World!")
```

### Disconnecting
```kotlin
val listener = signal.connect { data ->
    println("Data received: $data")
}

listener.disconnect(signal)
```

## Advanced Usage
### One-Time listener
Listener that is invoked only once.
Useful for scenarios where you only want a single response from a signal.
```kotlin
signal.connectOnce { data ->
    println("Data received once: $data")
}
```

### Utilities
#### Merge signals
Combine two signals into one.
This is useful when you want to handle emissions from multiple signals in one place.
```kotlin
val signalA = Signal<String>()
val signalB = Signal<String>()

val merged = merge(signalA, signalB)
merged.connect { println("Merged signal: $it") }
```

#### Zip signals
Zip two signals together.
This can be useful when you need to pair the emissions from two signals and handle them together.
```kotlin
val signalA = Signal<String>()
val signalB = Signal<Int>()

val zipped = zip(signalA, signalB)
zipped.connect { (a, b) -> println("Zipped signal: $a with $b") }
```

## Testing
A complete test suite is included in `SignalTest.kt`.
Run the tests to verify all functionality works as expected!
```shell
./gradlew test
```

## Contributing
Contributions are welcome!
Future improvements might include:

- Thread safety
- Replay functionality
- Exception-safe emissions
- Debugging tools

Feel free to fork and contribute 🎉

## License
[GNU GPLv3](LICENSE)