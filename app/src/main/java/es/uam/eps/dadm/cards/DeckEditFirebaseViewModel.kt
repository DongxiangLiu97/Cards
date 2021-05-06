package es.uam.eps.dadm.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeckEditFirebaseViewModel: ViewModel() {


    var reference = FirebaseDatabase.getInstance().getReference("mazos")
    private var _deck = MutableLiveData<Deck>()
    val deck: LiveData<Deck>
    get() = _deck

    private val deckId = MutableLiveData<String>()
    fun loadDeckId(id: String) {
        deckId.value = id
    }

    init {
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (deck in snapshot.children) {
                    var newDeck = deck.getValue(Deck::class.java)
                    if (newDeck != null) {
                        if (newDeck.deckId == deckId.value )
                            _deck.value = newDeck
                        break
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}