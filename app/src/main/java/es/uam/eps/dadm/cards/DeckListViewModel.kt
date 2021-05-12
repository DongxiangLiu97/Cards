package es.uam.eps.dadm.cards


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.database.DeckWithCards

class DeckListViewModel(application: Application) : AndroidViewModel(application) {


    private val context = getApplication<Application>().applicationContext

    private val userId= MutableLiveData<String?>()

    fun loadUserId(uid:String?){
        userId.value=uid
    }



    val decks: LiveData<List<Deck>> = Transformations.switchMap(userId){
        it?.let { it1 -> CardDatabase.getInstance(context).cardDao.getDecks(it1) }
    }

    val decksCards: LiveData<List<DeckWithCards>> = Transformations.switchMap(userId){
        it?.let { it1 ->CardDatabase.getInstance(context).cardDao.getDecksWithCardsFromUser(it) }
    }


}