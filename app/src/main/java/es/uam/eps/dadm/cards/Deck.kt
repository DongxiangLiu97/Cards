package es.uam.eps.dadm.cards

import androidx.room.ColumnInfo
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "decks_table")
data class Deck(
    @PrimaryKey val deckId: String = UUID.randomUUID().toString(),
    var name: String

){
    lateinit var user:String

    constructor() : this(
            UUID.randomUUID().toString(),
            ""
    )
}
