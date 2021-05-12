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

    private val userId= MutableLiveData<String>()

    private fun loadUserId(uid:String){
        userId.value=uid
    }

    fun getUserProfile() {
        // [START get_user_profile]
        val user = Firebase.auth.currentUser
        user?.let {
            val uid = user.uid
            loadUserId(uid)
        }
        // [END get_user_profile]
    }

    val decks: LiveData<List<Deck>> = Transformations.switchMap(userId){
        CardDatabase.getInstance(context).cardDao.getDecks(it)
    }


}