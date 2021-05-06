package es.uam.eps.dadm.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeckListFirebaseViewModel: ViewModel() {


    var reference = FirebaseDatabase.getInstance().getReference("mazos")
    private var _decks = MutableLiveData<List<Deck>>()
    val decks: LiveData<List<Deck>>
        get() = _decks

    init {
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfDecks: MutableList<Deck> = mutableListOf<Deck>()
                for (deck in snapshot.children) {
                    var newDeck = deck.getValue(Deck::class.java)
                    if (newDeck != null)
                        listOfDecks.add(newDeck)
                }
                _decks.value = listOfDecks
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}