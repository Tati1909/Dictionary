package com.example.dictionary.model.data

import com.google.gson.annotations.SerializedName

/**
 * DataModel — это модель наших данных, которые мы получаем из интернета.
 * Заглянем в API Skyeng и посмотрим, что нам нужно.
 * Из всего многообразия данных нам понадобится:
 * - само слово, которое нам нужно перевести
 * - набор значений для него (список Meanings)
 */
data class DataModel(
    @field:SerializedName("text") val text: String?,
    @field:SerializedName("meanings") val meanings: List<Meanings>?
)