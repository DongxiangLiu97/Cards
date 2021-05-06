package es.uam.eps.dadm.cards

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.databinding.FragmentDeckListBinding
import java.util.concurrent.Executors

private const val DATABASENAME = "mazos"
class DeckListFragment : Fragment() {
    private lateinit var binding: FragmentDeckListBinding
    private lateinit var adapter: DeckAdapter
    private val executor = Executors.newSingleThreadExecutor()

    private var reference = FirebaseDatabase
            .getInstance()
            .getReference(DATABASENAME)

    private val deckListViewModel by lazy {
        ViewModelProvider(this).get(DeckListFirebaseViewModel::class.java)
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

        adapter = DeckAdapter()

        deckListViewModel.decks.observe(
                viewLifecycleOwner,
                Observer {
                    adapter.data = it
                    adapter.notifyDataSetChanged()
                })

        binding.deckRecyclerView.adapter = adapter

        binding.newDeckFab.setOnClickListener {
            val deck = Deck()
            reference.child(deck.deckId).setValue(deck)
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
        }
        return true
    }
}