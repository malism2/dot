package com.malism.dot.ui.widget

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.malism.dot.utils.Log
import kotlin.jvm.JvmStatic

val LocalWindowSize = staticCompositionLocalOf<WindowSize> {
    error("LocalWindowSize")
}

class WindowSize private constructor(private val value: Int, val padding: Dp) {

    override fun toString(): String {
        val name =
            when (this) {
                COMPACT -> "COMPACT"
                MEDIUM -> "MEDIUM"
                EXPANDED -> "EXPANDED"
                LARGE -> "LARGE"
                else -> "UNKNOWN"
            }
        return "WindowWidthSizeClass: $name"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false

        val that = other as WindowSize

        return value == that.value
    }

    override fun hashCode(): Int {
        return value
    }

    fun isLarge() = this.value > MEDIUM.value

    companion object {
        @JvmStatic
        val COMPACT = WindowSize(0, 0.dp)
        @JvmStatic
        val MEDIUM = WindowSize(1, 64.dp)
        @JvmStatic
        val EXPANDED = WindowSize(2, 96.dp)
        @JvmStatic
        val LARGE = WindowSize(3, 320.dp)

        fun compute(dpWidth: Int): WindowSize {
            Log.d("window width $dpWidth")
            require(dpWidth >= 0) { "Width must be positive, received $dpWidth" }
            return when {
                dpWidth < 600 -> COMPACT
                dpWidth < 840 -> MEDIUM
                dpWidth < 1560 -> EXPANDED
                else -> LARGE
            }
        }
    }
}