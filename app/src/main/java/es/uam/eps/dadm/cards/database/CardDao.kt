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
    fun update(card: Card)

    @Query("DELETE FROM cards_table WHERE id = :id")
    fun deleteCard(id: String)
    @Query("DELETE FROM decks_table WHERE deckId = :id")
    fun deleteDeck(id: Long)

    @Update
    fun update(deck: Deck)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDeck(deck: Deck)

    @Query("SELECT * FROM decks_table WHERE deckId = :deckId")
    fun getDeck(deckId: Long): LiveData<Deck?>

    @Query("SELECT * FROM decks_table")
    fun getDecks(): LiveData<List<Deck>>

    @Transaction
    @Query("SELECT * FROM decks_table")
    fun getDecksWithCards(): LiveData<List<DeckWithCards>>

    @Transaction
    @Query("SELECT * FROM decks_table WHERE deckId = :deckId")
    fun getDeckWithCards(deckId: Long): LiveData<List<DeckWithCards>>

}