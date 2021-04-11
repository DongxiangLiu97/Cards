package es.uam.eps.dadm.cards

import android.app.Application
import timber.log.Timber

class CardsApplication: Application() {


    init {

        var deck=Deck("inglés")
        deck.cards.add(Card("To wake up", "Levantarse"))
        deck.cards.add(Card("To pick up", "Recoger"))
        decks.add(deck)

        deck=Deck("Frances")
        deck.cards.add(Card("question1", "Answer1"))
        deck.cards.add(Card("question2", "Answer2"))
        deck.cards.add(Card("question3", "Answer3"))
        decks.add(deck)

    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    companion object {

        fun getCard(cardId: String, deckId: String): Card {
            lateinit var card: Card
            decks.forEach{ deck ->
                if (deck.id== deckId) {
                    deck.cards.forEach{
                        if (it.id== cardId)
                            card=it
                    }
                }
            }
            return card
        }

        fun addCard(card: Card, deckId: String) {
            decks.forEach{
                if (it.id== deckId)
                    it.cards.add(card)
            }
        }

        fun addDeck(deck: Deck) {
            decks.add(deck)
        }

        fun getDeck(deckId: String): Deck {
            lateinit var deck: Deck

            decks.forEach{
                if (it.id== deckId)
                    deck=it
            }
            return deck

        }

        fun deleteDeck(deckId: String) {
            decks.forEach{ deck ->
                if (deck.id== deckId) {
                    decks.remove(deck)}
            }
        }

        fun getAllCards(): MutableList<Card> {
            var cards: MutableList<Card> = mutableListOf()
            decks.forEach{ deck ->
                cards.addAll(deck.cards)
            }
            return cards
        }

        fun deleteCard(cardId: String, deckId: String) {
            decks.forEach{ deck ->
                if (deck.id== deckId) {
                    deck.cards.forEach{
                        if (it.id== cardId)
                            deck.cards.remove(it)
                    }
                }
            }
        }


        var decks: MutableList<Deck> = mutableListOf<Deck>()
    }
}