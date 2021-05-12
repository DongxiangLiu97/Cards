package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.database.DeckWithCards
import java.time.LocalDateTime
import java.util.concurrent.Executors

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {


    private val context = getApplication<Application>().applicationContext
    var card: Card? = null

    private val userId= MutableLiveData<String?>()

    fun loadUserId(uid:String?){
        userId.value=uid
    }
    val decksCards: LiveData<List<DeckWithCards>> = Transformations.switchMap(userId){
        it?.let { CardDatabase.getInstance(context).cardDao.getDecksWithCardsFromUser(it) }
    }





}