package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import es.uam.eps.dadm.cards.database.CardDatabase

class CardListViewModel(application: Application) : AndroidViewModel(application) {


    private val context = getApplication<Application>().applicationContext
    val cards: LiveData<List<Card>> = CardDatabase.getInstance(context).cardDao.getCards()

    private val deckSelected= MutableLiveData<Long>()

    val cardsFromDecks: LiveData<List<Card>> = Transformations.switchMap(deckSelected){
        CardDatabase.getInstance(context).cardDao.getCardsFromDeck(it)
    }
    fun loadDeckId(deckId: Long){
        deckSelected.value=deckId
    }
}