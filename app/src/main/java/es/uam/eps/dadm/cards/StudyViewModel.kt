package es.uam.eps.dadm.cards

import android.app.Application
import android.text.NoCopySpan
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.database.DeckWithCards
import java.time.LocalDateTime
import java.util.concurrent.Executors

class StudyViewModel(application: Application) : AndroidViewModel(application) {
    private val executor = Executors.newSingleThreadExecutor()

    private val context = getApplication<Application>().applicationContext
    var card: Card? = null

    private val userId= MutableLiveData<String?>()

    fun loadUserId(uid:String?){
        userId.value=uid
    }
    private val decksCards: LiveData<List<DeckWithCards>> = Transformations.switchMap(userId){
        it?.let { CardDatabase.getInstance(context).cardDao.getDecksWithCardsFromUser(it) }
    }


    var cards: LiveData<List<Card>> = Transformations.map(decksCards, ::getCards)

    private fun getCards(decksWithCardsList: List<DeckWithCards>): List<Card> {
        val cardsList: MutableList<Card> = mutableListOf()
        decksWithCardsList.forEach {
            cardsList += it.cards
        }
        return cardsList
    }
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