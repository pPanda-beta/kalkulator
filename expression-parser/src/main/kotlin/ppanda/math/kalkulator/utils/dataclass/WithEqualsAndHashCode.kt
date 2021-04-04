package ppanda.math.kalkulator.utils.dataclass

import java.util.*
import kotlin.reflect.cast

interface WithEqualsAndHashCode {

    fun propsForEquality(): Array<Any?>

    fun standardHashcode(): Int = Objects.hash(propsForEquality())

    fun standardEquality(other: Any?): Boolean = when {
        this === other -> true
        this::class != other?.let { it::class } -> false
        else -> propsForEquality()
            .zip((this::class.cast(other)).propsForEquality())
            .all { it.first == it.second }
    }
}
