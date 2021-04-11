package es.uam.eps.dadm.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import es.uam.eps.dadm.cards.databinding.FragmentDeckListBinding



class DeckListFragment : Fragment() {
    private lateinit var binding: FragmentDeckListBinding
    private lateinit var adapter: DeckAdapter

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
            val deck = Deck("")
            CardsApplication.addDeck(deck)
            it.findNavController().navigate(DeckListFragmentDirections
                    .actionDeckListFragmentToDeckEditFragment(deck.id))
        }


        return binding.root
    }
}