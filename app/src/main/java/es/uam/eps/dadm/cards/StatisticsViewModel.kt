package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.database.DeckWithCards

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    var decks: LiveData<List<DeckWithCards>> = CardDatabase.getInstance(context).cardDao.getDecksWithCards()

    private val deckSelected = MutableLiveData<String>()

    val deckWithCards: LiveData<List<DeckWithCards>> = Transformations.switchMap(deckSelected) {
        CardDatabase.getInstance(context).cardDao.getDeckWithCards(it)
    }

    fun loadDeckId(id: String) {
        deckSelected.value = id
    }

}