package com.sujeong.pillo.common.manager

interface PreferencesManager {
    fun read(key: String, defaultValue: Long): Long
    fun write(key: String, value: Long)
    fun clear(key: String)
}