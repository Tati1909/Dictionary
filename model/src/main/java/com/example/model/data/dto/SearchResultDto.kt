package com.example.model.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DataModel — это модель наших данных, которые мы получаем из интернета.
 * Заглянем в API Skyeng и посмотрим, что нам нужно.
 * Из всего многообразия данных нам понадобится:
 * - само слово, которое нам нужно перевести
 * - набор значений для него (список Meanings)
 */
data class SearchResultDto(
    @field:SerializedName("text") val text: String?,
    @field:SerializedName("meanings") val meanings: List<MeaningsDto>?
)