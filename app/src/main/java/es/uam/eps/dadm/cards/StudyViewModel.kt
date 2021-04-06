package es.uam.eps.dadm.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime

class StudyViewModel : ViewModel() {
    var card: Card? = null
    var cards: MutableList<Card> = mutableListOf<Card>()
    private val _cardsLeft = MutableLiveData<Int>()
    val cardsLeft: LiveData<Int>
        get() = _cardsLeft
    init {
        cards = CardsApplication.cards
        card = random_card()
        _cardsLeft.value = cards.size

    }

    fun update(quality: Int) {
        card?.quality = quality
        card?.update(LocalDateTime.now())
        card = random_card()
        _cardsLeft.value = cardsLeft.value?.minus(1)
    }

    private fun random_card() = try {
        cards.filter { card ->
            card.isDue(LocalDateTime.now())
        }.random()
    } catch (e: NoSuchElementException) {
        null
    }
}