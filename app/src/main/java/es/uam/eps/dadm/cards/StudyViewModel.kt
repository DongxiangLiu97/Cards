package es.uam.eps.dadm.cards

import android.app.Application
import android.text.NoCopySpan
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import es.uam.eps.dadm.cards.database.CardDatabase
import java.time.LocalDateTime
import java.util.concurrent.Executors

class StudyViewModel(application: Application) : AndroidViewModel(application) {
    private val executor = Executors.newSingleThreadExecutor()

    private val context = getApplication<Application>().applicationContext
    var card: Card? = null



    var cards: LiveData<List<Card>> = CardDatabase.getInstance(context).cardDao.getCards()
    var dueCard: LiveData<Card?> =Transformations.map(cards, ::due)
    var cardsLeft: LiveData<Int> =Transformations.map(cards, ::left)

    private fun due(cards: List<Card>) = try {
        cards.filter { card -> card.isDue(LocalDateTime.now()) }.random()
    } catch (e: Exception) {
        null
    }

    private fun left(cards: List<Card>) =
        cards.filter { card -> card.isDue(LocalDateTime.now()) }.size

    fun update(quality: Int) {
        card?.quality = quality
        card?.update(LocalDateTime.now())

        executor.execute {
            CardDatabase.getInstance(context).cardDao.updateCard(card!!)
        }
    }
}