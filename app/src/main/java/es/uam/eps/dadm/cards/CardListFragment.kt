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
import es.uam.eps.dadm.cards.databinding.FragmentCardListBinding
import java.util.concurrent.Executors

private const val DATABASENAME = "tarjetas"

class CardListFragment : Fragment() {
    private lateinit var binding: FragmentCardListBinding
    private lateinit var adapter: CardAdapter


    private val cardListViewModel by lazy {
        ViewModelProvider(this).get(CardListFirebaseViewModel::class.java)
    }
    private var reference = FirebaseDatabase
            .getInstance()
            .getReference(DATABASENAME)


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
        adapter.deckId=args.deckId
        cardListViewModel.cards.observe(
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
            reference.child(card.id).setValue(card)
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
        }
        return true
    }
}