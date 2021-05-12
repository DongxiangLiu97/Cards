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
import es.uam.eps.dadm.cards.databinding.FragmentCardListBinding
import java.util.concurrent.Executors


private const val DATABASENAME = "CardApplication"

class CardListFragment : Fragment() {



    private lateinit var binding: FragmentCardListBinding
    private lateinit var adapter: CardAdapter
    private val executor = Executors.newSingleThreadExecutor()
    lateinit var deckId :String
    private lateinit var user: FirebaseUser

    private lateinit var decksCards: List<DeckWithCards>

    private val cardListViewModel by lazy {
        ViewModelProvider(this).get(CardListViewModel::class.java)
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
            R.layout.fragment_card_list,
            container,
            false
        )
        user = Firebase.auth.currentUser
        adapter = CardAdapter()
        val args = CardListFragmentArgs.fromBundle(requireArguments())
        deckId=args.deckId
        adapter.deckId=args.deckId
        cardListViewModel.loadDeckId(args.deckId)

        cardListViewModel.loadUserId(user.uid)
        cardListViewModel.decksCards.observe(
            viewLifecycleOwner,
            Observer {
                decksCards=it
            }
        )
        cardListViewModel.cardsFromDecks.observe(
                viewLifecycleOwner,
                Observer {
                    adapter.data = it
                    adapter.notifyDataSetChanged()
                })

        adapter.deckId=args.deckId
        binding.cardRecyclerView.adapter = adapter

        binding.newCardFab.setOnClickListener {
            val card = Card()
            card.deckId=adapter.deckId
            executor.execute {
                CardDatabase.getInstance(requireContext()).cardDao.addCard(card)
            }
            it.findNavController().navigate(
                    CardListFragmentDirections
                            .actionCardListFragmentToCardEditFragment(card.id,adapter.deckId)
            )
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
                findNavController().navigate(CardListFragmentDirections.actionCardListFragmentToLoginFragment())

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