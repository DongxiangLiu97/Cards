package es.uam.eps.dadm.cards

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.database.DeckWithCards
import es.uam.eps.dadm.cards.databinding.FragmentDeckListBinding
import java.util.concurrent.Executors


private const val DATABASENAME = "CardApplication"
class DeckListFragment : Fragment() {

    private lateinit var binding: FragmentDeckListBinding
    private lateinit var adapter: DeckAdapter
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var user: FirebaseUser
    private lateinit var decksCards: List<DeckWithCards>

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
    ): View {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_deck_list,
                container,
                false
        )
        user = Firebase.auth.currentUser

        adapter = DeckAdapter()
        deckListViewModel.loadUserId(user.uid)
        deckListViewModel.decksCards.observe(
            viewLifecycleOwner,
            Observer {
                decksCards=it
            }
        )

        deckListViewModel.decks.observe(
                viewLifecycleOwner,
                Observer {
                    adapter.data = it
                    adapter.notifyDataSetChanged()
                })

        binding.deckRecyclerView.adapter = adapter

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
                findNavController().navigate(DeckListFragmentDirections.actionDeckListFragmentToCardSettingsFragment())
            }
            R.id.updateLocalData->{

                decksCards.forEach {
                    executor.execute {
                        CardDatabase.getInstance(requireContext()).cardDao.deleteAllCardsFromDeck(it.deck.deckId)
                        CardDatabase.getInstance(requireContext()).cardDao.deleteDeck(it.deck.deckId)
                    }
                }
                getDecksFromFirebase()
                getCardsFirebase()
            }
            R.id.updateFirebaseData->{
                val reference = FirebaseDatabase
                    .getInstance()
                    .getReference(DATABASENAME)

                reference.child(user.uid).child("decks").setValue(null)
                reference.child(user.uid).child("cards").setValue(null)


                decksCards.forEach { it ->
                    reference.child(user.uid).child("decks").child(it.deck.deckId).setValue(it.deck)

                    val cards = it.cards
                    cards.forEach{
                        reference.child(user.uid).child("cards").child(it.deckId).setValue(it)
                    }

                }

            }
            R.id.log_out->{

                // [START auth_sign_out]
                Firebase.auth.signOut()
                // [END auth_sign_out]
                findNavController().navigate(DeckListFragmentDirections.actionDeckListFragmentToLoginFragment())

            }

        }
        return true
    }
    private fun getDecksFromFirebase(){
        val reference = FirebaseDatabase
            .getInstance()
            .getReference(DATABASENAME).child(user.uid).child("decks")

        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (deck in snapshot.children) {
                    val newDeck = deck.getValue(Deck::class.java)
                    if (newDeck != null){
                        executor.execute {
                            CardDatabase.getInstance(requireContext()).cardDao.addDeck(newDeck)
                        }
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    private fun getCardsFirebase(){
        val reference = FirebaseDatabase
            .getInstance()
            .getReference(DATABASENAME).child(user.uid).child("cards")

        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (card in snapshot.children) {
                    val newCard = card.getValue(Card::class.java)
                    if (newCard != null) {
                        executor.execute{
                            CardDatabase.getInstance(requireContext()).cardDao.addCard(newCard)
                        }
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}