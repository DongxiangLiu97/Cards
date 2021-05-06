package es.uam.eps.dadm.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CardEditFirebaseViewModel: ViewModel() {


    var reference = FirebaseDatabase.getInstance().getReference("tarjetas")
    private var _card = MutableLiveData<Card>()
    val card: LiveData<Card>
        get() = _card

    private val cardId = MutableLiveData<String>()
    fun loadCardId(id: String) {
        cardId.value = id
    }

    init {
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (card in snapshot.children) {
                    var newCard = card.getValue(Card::class.java)
                    if (newCard != null) {
                        if (newCard.id ==cardId.value )
                            _card.value = newCard
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