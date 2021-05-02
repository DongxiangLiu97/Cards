package es.uam.eps.dadm.cards

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks_table")
data class Deck(
    @PrimaryKey val deckId: Long,
    var name: String
)
