package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.database.DeckWithCards

class CardListViewModel(application: Application) : AndroidViewModel(application) {


    private val context = getApplication<Application>().applicationContext
    val cards: LiveData<List<Card>> = CardDatabase.getInstance(context).cardDao.getCards()

    private val deckSelected= MutableLiveData<String>()

    val cardsFromDecks: LiveData<List<Card>> = Transformations.switchMap(deckSelected){
        CardDatabase.getInstance(context).cardDao.getCardsFromDeck(it)
    }
    fun loadDeckId(deckId: String){
        deckSelected.value=deckId
    }
    private val userId= MutableLiveData<String?>()

    fun loadUserId(uid:String?){
        userId.value=uid
    }
    val decksCards: LiveData<List<DeckWithCards>> = Transformations.switchMap(userId){
        it?.let { it1 ->CardDatabase.getInstance(context).cardDao.getDecksWithCardsFromUser(it) }
    }
}