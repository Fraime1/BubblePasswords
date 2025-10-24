package com.bubble.passwrosoft.util

import kotlin.random.Random

object PasswordGenerator {
    private const val UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWER = "abcdefghijklmnopqrstuvwxyz"
    private const val DIGITS = "0123456789"
    private const val SYMBOLS = "!@#${'$'}%^&*()-_=+[]{};:,.?/"

    fun generate(
        length: Int,
        useUpper: Boolean,
        useLower: Boolean,
        useDigits: Boolean,
        useSymbols: Boolean,
    ): String {
        val pool = buildString {
            if (useUpper) append(UPPER)
            if (useLower) append(LOWER)
            if (useDigits) append(DIGITS)
            if (useSymbols) append(SYMBOLS)
        }
        if (pool.isEmpty()) return ""
        return (0 until length).map { pool[Random.nextInt(pool.length)] }.joinToString("")
    }
}


