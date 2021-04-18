package es.uam.eps.dadm.cards.database

import androidx.room.Embedded
import androidx.room.Relation
import es.uam.eps.dadm.cards.Card
import es.uam.eps.dadm.cards.Deck

data class DeckWithCards(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "deckId",
        entityColumn = "deckId"
    )

    val cards: List<Card>
)