package es.uam.eps.dadm.cards

import android.app.Application
import timber.log.Timber

class CardsApplication: Application() {


    init {
        cards.add(Card("To wake up", "Levantarse"))
        cards.add(Card("To pick up", "Recoger"))

    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        fun numberOfCardsLeft(): Int {

            return cards.size

        }

        fun getCard(cardId: String): Card {
            lateinit var card: Card

            cards.forEach{
                if (it.id== cardId)
                    card=it
            }
            return card
        }

        fun addCard(card: Card) {
            cards.add(card)
        }

        var cards: MutableList<Card> = mutableListOf<Card>()
    }
}