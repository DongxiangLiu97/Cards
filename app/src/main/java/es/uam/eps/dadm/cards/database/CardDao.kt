package es.uam.eps.dadm.cards.database

import androidx.lifecycle.LiveData
import androidx.room.*
import es.uam.eps.dadm.cards.Card
import es.uam.eps.dadm.cards.Deck

@Dao
interface CardDao {
    @Query("SELECT * FROM cards_table")
    fun getCards(): LiveData<List<Card>>

    @Query("SELECT * FROM cards_table WHERE id = :id")
    fun getCard(id: String): LiveData<Card?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCard(card: Card)
    @Update
    fun updateCard(card: Card)

    @Query("DELETE FROM cards_table WHERE id = :id")
    fun deleteCard(id: String)

    @Query("SELECT * FROM cards_table WHERE deckId = :DeckId")
    fun getCardsFromDeck(DeckId: String):LiveData<List<Card>>


    @Query("DELETE FROM decks_table WHERE deckId = :id")
    fun deleteDeck(id: String)

    @Update
    fun updateDeck(deck: Deck)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDeck(deck: Deck)

    @Query("SELECT * FROM decks_table WHERE deckId = :deckId")
    fun getDeck(deckId: String): LiveData<Deck?>

    @Query("SELECT * FROM decks_table")
    fun getDecks(): LiveData<List<Deck>>

    @Transaction
    @Query("SELECT * FROM decks_table")
    fun getDecksWithCards(): LiveData<List<DeckWithCards>>

    @Transaction
    @Query("SELECT * FROM decks_table WHERE name = :name")
    fun getDeckWithCards(name: String): LiveData<List<DeckWithCards>>


}