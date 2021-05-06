package es.uam.eps.dadm.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CardListFirebaseViewModel(): ViewModel() {


    var reference = FirebaseDatabase.getInstance().getReference("tarjetas")
    private var _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>>
        get() = _cards

    init {
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfCards: MutableList<Card> = mutableListOf<Card>()
                for (card in snapshot.children) {
                    var newCard = card.getValue(Card::class.java)
                    if (newCard != null)
                        listOfCards.add(newCard)
                }
                _cards.value = listOfCards
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}