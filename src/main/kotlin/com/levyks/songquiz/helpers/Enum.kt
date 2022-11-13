package com.levyks.songquiz.helpers

inline fun <reified T : Enum<T>> tryParse(type: String): T? {
    return try {
        java.lang.Enum.valueOf(T::class.java, type)
    } catch (e: Exception) {
        null
    }
}