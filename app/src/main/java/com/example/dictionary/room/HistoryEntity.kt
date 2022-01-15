package com.example.dictionary.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * unique = true означает, что в БД не будут сохраняться повторяющиеся слова.
 * Далее всё стандартно для любой БД. Мы храним слово и его перевод (перевод не сохраняется в текущем приложении,
 * но вы можете имплементировать дополнительный функционал самостоятельно)
 */
@Entity(indices = [Index(value = arrayOf("word"), unique = true)])
class HistoryEntity(

    @field:PrimaryKey
    @field:ColumnInfo(name = "word")
    var word: String, @field:ColumnInfo(name = "description")
    var description: String?
)