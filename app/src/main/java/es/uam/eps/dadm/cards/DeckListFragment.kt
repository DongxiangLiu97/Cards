package es.uam.eps.dadm.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.databinding.FragmentDeckListBinding
import java.util.concurrent.Executors


class DeckListFragment : Fragment() {
    private lateinit var binding: FragmentDeckListBinding
    private lateinit var adapter: DeckAdapter
    private val executor = Executors.newSingleThreadExecutor()


    private val deckListViewModel by lazy {
        ViewModelProvider(this).get(DeckListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        adapter.data = CardsApplication.decks
        binding.deckRecyclerView.adapter = adapter

        binding.newDeckFab.setOnClickListener {
            val deck = Deck((adapter.data.size+1).toLong(),"")
            executor.execute {
                CardDatabase.getInstance(requireContext()).cardDao.addDeck(deck)
            }
            it.findNavController().navigate(DeckListFragmentDirections
                    .actionDeckListFragmentToDeckEditFragment(deck.deckId))
        }
       deckListViewModel.decks.observe(
            viewLifecycleOwner,
            Observer {
                adapter.data = it
                adapter.notifyDataSetChanged()
            })

        return binding.root
    }
}