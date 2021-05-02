package es.uam.eps.dadm.cards

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.databinding.FragmentDeckEditBinding
import java.util.concurrent.Executors


class DeckEditFragment : Fragment() {
    lateinit var binding: FragmentDeckEditBinding
    private val executor = Executors.newSingleThreadExecutor()
    lateinit var deck: Deck
    lateinit var name: String

    private val viewModel by lazy {
        ViewModelProvider(this).get(DeckEditViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_deck_edit,
                container,
                false
        )

        val args = DeckEditFragmentArgs.fromBundle(requireArguments())

        viewModel.loadDeckId(args.deckId)
        viewModel.deck.observe(viewLifecycleOwner) {
            deck = it
            binding.deck = deck
            name = deck.name
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val nameTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                deck.name = s.toString()
            }
        }



        binding.nameEditText.addTextChangedListener(nameTextWatcher)

        // Añade escuchadores OnClickListener para los botones

        binding.acceptDeckEditButton.setOnClickListener {
            executor.execute {
                val cardDatabase = CardDatabase.getInstance(requireContext())
                cardDatabase.cardDao.updateDeck(deck)
            }
            it.findNavController()
                    .navigate(DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment())

        }
        binding.cancelDeckEditButton.setOnClickListener {
            deck.name=name
            it.findNavController()
                    .navigate(DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment())
        }
        binding.deckDeleteButton.setOnClickListener {
            val id = deck.deckId
            executor.execute {
                val cardDatabase = CardDatabase.getInstance(requireContext())
                cardDatabase.cardDao.deleteDeck(id)
            }
            it.findNavController()
                    .navigate(DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment())
        }
    }
}