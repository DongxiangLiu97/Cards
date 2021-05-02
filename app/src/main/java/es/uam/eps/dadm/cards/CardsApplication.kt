package es.uam.eps.dadm.cards

import android.app.Application
import es.uam.eps.dadm.cards.database.CardDatabase
import java.util.concurrent.Executors

class CardsApplication: Application() {
    private val executor = Executors.newSingleThreadExecutor()


    override fun onCreate() {
        super.onCreate()

        //decks.add(Deck(1, "Inglés"))
        //decks.add(Deck(2, "Francés"))
        val cardDatabase = CardDatabase.getInstance(context = this)
        /*executor.execute {
            cardDatabase.cardDao.addCard(Card("To wake up", "Despertarse", deckId = 1))
            cardDatabase.cardDao.addCard(Card("To give in", "Dar el brazo a torcer", deckId = 1))
            cardDatabase.cardDao.addCard(Card("Coche", "Voiture", deckId = 2))
        }*/
    }


}