package es.uam.eps.dadm.cards


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.databinding.FragmentCardListBinding
import java.util.concurrent.Executors


private const val DATABASENAME = "Tarjetas"

class CardListFragment : Fragment() {



    private lateinit var binding: FragmentCardListBinding
    private lateinit var adapter: CardAdapter
    private val executor = Executors.newSingleThreadExecutor()
    lateinit var deckId :String
    lateinit var reference: DatabaseReference

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
        adapter = CardAdapter()
        val args = CardListFragmentArgs.fromBundle(requireArguments())
        deckId=args.deckId

        reference = FirebaseDatabase
                .getInstance()
                .getReference("Tarjetas").child(deckId).child("cards")
        adapter.deckId=args.deckId
        cardListViewModel.loadDeckId(args.deckId)

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
            .getReference(DATABASENAME).child("mazos")
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
            .getReference(DATABASENAME).child("cards")

        reference.addValueEventListener(object: ValueEventListener {
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