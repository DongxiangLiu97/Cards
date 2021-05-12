package es.uam.eps.dadm.cards

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.databinding.FragmentDeckListBinding
import java.util.concurrent.Executors



private const val DATABASENAME = "Tarjetas"
class DeckListFragment : Fragment() {

    private lateinit var binding: FragmentDeckListBinding
    private lateinit var adapter: DeckAdapter
    private val executor = Executors.newSingleThreadExecutor()



    private val deckListViewModel by lazy {
        ViewModelProvider(this).get(DeckListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_deck_list,
                container,
                false
        )
        val user = Firebase.auth.currentUser

        adapter = DeckAdapter()
        deckListViewModel.getUserProfile()
        deckListViewModel.decks.observe(
                viewLifecycleOwner,
                Observer {
                    adapter.data = it
                    adapter.notifyDataSetChanged()
                })

        binding.deckRecyclerView.adapter = adapter
        if (user==null){
            Toast.makeText(context,getString(R.string.NoUser),Toast.LENGTH_SHORT).show()
            binding.newDeckFab.visibility=View.INVISIBLE
        }
        binding.newDeckFab.setOnClickListener {
            val deck = Deck()
            deck.user=user.uid
            executor.execute {
                CardDatabase.getInstance(requireContext()).cardDao.addDeck(deck)
            }
            it.findNavController().navigate(DeckListFragmentDirections
                    .actionDeckListFragmentToDeckEditFragment(deck.deckId))
        }


        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_card_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                findNavController().navigate(CardListFragmentDirections.actionCardListFragmentToCardSettingsFragment())
            }
            R.id.updateLocalData->{
                CardDatabase.getInstance(requireContext()).cardDao.deleteAllDecks()
                CardDatabase.getInstance(requireContext()).cardDao.deleteAllCards()
                getDecksFromFirebase()
                getCardsFirebase()
            }
            R.id.updateFirebaseData->{

            }
            R.id.log_out->{

                    // [START auth_sign_out]
                    Firebase.auth.signOut()
                    // [END auth_sign_out]
            }

        }
        return true
    }
    private fun getDecksFromFirebase(){
        var decks = MutableLiveData<List<Deck>>()

        var reference = FirebaseDatabase
                .getInstance()
                .getReference(DATABASENAME).child("decks")
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfDecks: MutableList<Deck> = mutableListOf<Deck>()
                for (deck in snapshot.children) {
                    var newDeck = deck.getValue(Deck::class.java)
                    if (newDeck != null)
                        listOfDecks.add(newDeck)
                }
                decks.value = listOfDecks
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        decks.value?.forEach {
            CardDatabase.getInstance(requireContext()).cardDao.addDeck(it)
        }
    }
    private fun getCardsFirebase(){
        var cards = MutableLiveData<List<Card>>()

        var reference = FirebaseDatabase
                .getInstance()
                .getReference(DATABASENAME).child("mazos")
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var listOfCards: MutableList<Card> = mutableListOf<Card>()
                for (card in snapshot.children) {
                    var newCard = card.getValue(Card::class.java)
                    if (newCard != null)
                        listOfCards.add(newCard)
                }
                cards.value = listOfCards
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        cards.value?.forEach {
            CardDatabase.getInstance(requireContext()).cardDao.addCard(it)
        }
    }
}