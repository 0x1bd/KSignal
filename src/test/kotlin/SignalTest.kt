import de.kvxd.ksignal.*
import io.github.kvxd.ksignal.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SignalTest {

    @Test
    fun testSignalEmit() {
        val signal = Signal<Int>()
        var result = 0

        signal.connect { result = it }
        signal.emit(42)

        assertEquals(42, result)
    }

    @Test
    fun testMap() {
        val signal = Signal<Int>()
        val mapped = signal.map { it * 2 }

        var result = 0
        mapped.connect { result = it }

        signal.emit(3)

        assertEquals(6, result)
    }

    @Test
    fun testZip() {
        val a = Signal<Int>()
        val b = Signal<String>()

        val zipped = zip(a, b)

        val results = mutableListOf<Pair<Int, String>>()
        zipped.connect { results.add(it) }

        a.emit(1)
        b.emit("one")
        a.emit(2)
        b.emit("two")

        assertEquals(listOf(1 to "one", 2 to "two"), results)

        // Test interleaved emissions
        a.emit(3)
        b.emit("three")
        assertEquals(listOf(1 to "one", 2 to "two", 3 to "three"), results)
    }

    @Test
    fun testFilter() {
        val signal = Signal<Int>()
        val filtered = signal.filter { it % 2 == 0 }

        var result = -1
        filtered.connect { result = it }

        signal.emit(1)
        assertEquals(-1, result) // Should not change

        signal.emit(2)
        assertEquals(2, result)

        signal.emit(3)
        assertEquals(2, result) // Should still be 2, as 3 is not emitted
    }

    @Test
    fun testFilterIsInstance() {
        val signal = Signal<Any>()
        val filtered = signal.filterIsInstance<String>()

        val results = mutableListOf<String>()
        filtered.connect { results.add(it) }

        signal.emit(123)
        signal.emit("hello")
        signal.emit(456)
        signal.emit("world")

        assertEquals(listOf("hello", "world"), results)
    }

    @Test
    fun testTake() {
        val signal = Signal<Int>()
        val taken = signal.take(2)

        val results = mutableListOf<Int>()
        taken.connect { results.add(it) }

        signal.emit(1)
        signal.emit(2)
        signal.emit(3)

        assertEquals(listOf(1, 2), results)
    }

    @Test
    fun testOnEach() {
        val signal = Signal<Int>()
        val results = mutableListOf<Int>()

        // Use onEach to perform action on each emission
        signal.onEach { results.add(it * 2) }

        signal.emit(1)
        signal.emit(2)
        signal.emit(3)

        // Expect doubled values
        assertEquals(listOf(2, 4, 6), results)
    }

    @Test
    fun testMerge() {
        val a = Signal<Int>()
        val b = Signal<Int>()

        val merged = merge(a, b)

        val results = mutableListOf<Int>()
        merged.connect { results.add(it) }

        a.emit(1)
        b.emit(2)

        assertEquals(listOf(1, 2), results)

        // Test interleaved emissions
        a.emit(3)
        b.emit(4)
        assertEquals(listOf(1, 2, 3, 4), results)
    }

    @Test
    fun testDistinctUntilChanged() {
        val signal = Signal<Int>()
        val distinct = signal.distinctUntilChanged()

        val results = mutableListOf<Int>()
        distinct.connect { results.add(it) }

        signal.emit(1)
        signal.emit(1)
        signal.emit(2)
        signal.emit(2)
        signal.emit(3)

        assertEquals(listOf(1, 2, 3), results)

        // Test emitting same value again after distinct emission
        signal.emit(3)
        assertEquals(listOf(1, 2, 3), results) // 3 should not be added again
    }

    @Test
    fun testFold() {
        val signal = Signal<Int>()
        val folded = signal.fold(0) { acc, value -> acc + value }

        val results = mutableListOf<Int>()
        folded.connect { results.add(it) }

        signal.emit(1)
        signal.emit(2)
        signal.emit(3)

        assertEquals(listOf(1, 3, 6), results)
    }

    @Test
    fun testFlatten() {
        val outer = Signal<Signal<Int>>()
        val inner1 = Signal<Int>()
        val inner2 = Signal<Int>()
        val flattened = outer.flatten()

        val results = mutableListOf<Int>()
        flattened.connect { results.add(it) }

        outer.emit(inner1)
        inner1.emit(1)
        outer.emit(inner2)
        inner2.emit(2)
        inner1.emit(3)

        assertEquals(listOf(1, 2, 3), results)

        // Test if new inner signals can still emit
        val inner3 = Signal<Int>()
        outer.emit(inner3)
        inner3.emit(4)
        assertEquals(listOf(1, 2, 3, 4), results)
    }

    @Test
    fun testConnectOnce() {
        val signal = Signal<Int>()
        val results = mutableListOf<Int>()

        signal.connectOnce { results.add(it) }

        signal.emit(10)
        signal.emit(20)

        assertEquals(listOf(10), results)
    }

    @Test
    fun testDisconnect() {
        val signal = Signal<Int>()
        val results = mutableListOf<Int>()

        val listener = signal.connect { results.add(it) }

        signal.emit(1)
        listener.disconnect(signal)
        signal.emit(2)

        assertEquals(listOf(1), results)
    }

    @Test
    fun testClear() {
        val signal = Signal<Int>()
        var result = -1

        signal.connect { result = it }

        signal.emit(42)
        assertEquals(42, result)

        signal.clear()

        signal.emit(100)

        assertEquals(42, result)
    }

    @Test
    fun testNoEmissionBeforeConnect() {
        val signal = Signal<Int>()
        var result = -1

        signal.emit(42) // Emitting before connection
        signal.connect { result = it }

        assertEquals(-1, result) // result should not change as emission was before connection
    }
}
