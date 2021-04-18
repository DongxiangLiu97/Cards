package es.uam.eps.dadm.cards

import android.app.Application
import es.uam.eps.dadm.cards.database.CardDatabase
import java.util.concurrent.Executors

class CardsApplication: Application() {
    private val executor = Executors.newSingleThreadExecutor()

    init {
        decks.add(Deck(1, "Inglés"))
        decks.add(Deck(2, "Francés"))
    }

    override fun onCreate() {
        super.onCreate()
        val cardDatabase = CardDatabase.getInstance(context = this)
        executor.execute {
            cardDatabase.cardDao.addCard(Card("To wake up", "Despertarse", deckId = 1))
            cardDatabase.cardDao.addCard(Card("To give in", "Dar el brazo a torcer", deckId = 1))
            cardDatabase.cardDao.addCard(Card("Coche", "Voiture", deckId = 2))
        }
    }

    companion object {
        var cards: MutableList<Card> = mutableListOf<Card>()
        var decks: MutableList<Deck> = mutableListOf<Deck>()

        fun getCard(id: String): Card {
            return cards.filter { it.id == id }[0]
        }
        fun addCard(card: Card) {
            cards.add(card)
        }
        fun deleteCard(id: String) {
            cards.remove(getCard(id))
        }

        fun getDeck(deckId: Long): Deck {
            return decks.filter { it.deckId == deckId }[0]
        }
    }
}