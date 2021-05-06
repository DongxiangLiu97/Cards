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
import com.google.firebase.database.FirebaseDatabase
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.databinding.FragmentDeckEditBinding
import java.util.concurrent.Executors


class DeckEditFragment : Fragment() {
    lateinit var binding: FragmentDeckEditBinding
    lateinit var deck: Deck
    lateinit var name: String

    private var reference = FirebaseDatabase
            .getInstance()
            .getReference("mazos")


    private val viewModel by lazy {
        ViewModelProvider(this).get(DeckEditFirebaseViewModel::class.java)
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
            reference.child(deck.deckId).setValue(deck)
            it.findNavController()
                    .navigate(DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment())

        }
        binding.cancelDeckEditButton.setOnClickListener {
            deck.name=name
            if (deck.name == "name" ){
                reference.child(deck.deckId).removeValue()
            }
            it.findNavController()
                    .navigate(DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment())
        }
        binding.deckDeleteButton.setOnClickListener {

            reference.child(deck.deckId).removeValue()

            it.findNavController()
                    .navigate(DeckEditFragmentDirections.actionDeckEditFragmentToDeckListFragment())
        }
    }
}