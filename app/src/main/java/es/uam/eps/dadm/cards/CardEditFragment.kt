package es.uam.eps.dadm.cards

import android.content.Context
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
import es.uam.eps.dadm.cards.databinding.FragmentCardEditBinding
import java.util.concurrent.Executors
import kotlin.properties.Delegates

class CardEditFragment : Fragment() {
    private val executor = Executors.newSingleThreadExecutor()
    lateinit var binding: FragmentCardEditBinding
    lateinit var card: Card
    lateinit var question: String
    lateinit var answer: String
    var deckId by Delegates.notNull<Long>()


    private val viewModel by lazy {
        ViewModelProvider(this).get(CardEditViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_card_edit,
            container,
            false
        )

        val args = CardEditFragmentArgs.fromBundle(requireArguments())
        deckId=args.deckId
        viewModel.loadCardId(args.cardId)
        viewModel.card.observe(viewLifecycleOwner) {
            card = it
            binding.card = card
            question = card.question
            answer = card.answer
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val questionTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                card.question = s.toString()
            }
        }

        val answerTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                card.answer = s.toString()
            }
        }

        binding.questionEditText.addTextChangedListener(questionTextWatcher)
        binding.answerEditText.addTextChangedListener(answerTextWatcher)
        binding.acceptCardEditButton.setOnClickListener {
            executor.execute {
                val cardDatabase = CardDatabase.getInstance(requireContext())
                cardDatabase.cardDao.update(card)
            }

            it.findNavController().navigate(CardEditFragmentDirections.actionCardEditFragmentToCardListFragment(deckId))
        }
        binding.cancelCardEditButton.setOnClickListener {
            card.question = question
            card.answer = answer
            if (card.question == "" || card.answer == "")
                executor.execute {
                    val cardDatabase = CardDatabase.getInstance(requireContext())
                    cardDatabase.cardDao.deleteCard(card.id)
                }

            it.findNavController().navigate(CardEditFragmentDirections.actionCardEditFragmentToCardListFragment(deckId))
        }
    }
}