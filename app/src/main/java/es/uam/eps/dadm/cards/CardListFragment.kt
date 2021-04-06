package es.uam.eps.dadm.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import es.uam.eps.dadm.cards.databinding.FragmentCardListBinding

private val TAG: String = "CardListFragment"

class CardListFragment : Fragment() {
    private lateinit var binding: FragmentCardListBinding
    private lateinit var adapter: CardAdapter

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
        adapter.data = CardsApplication.cards
        binding.cardRecyclerView.adapter = adapter

        // Ajusta un escuchador OnClickListener
        // al botón flotate
        binding.newCardFab.setOnClickListener {
            val card = Card("", "")
            CardsApplication.addCard(card)

            // Elimina el siguiente código
            Snackbar.make(it, "Tarjeta añadida", Snackbar.LENGTH_SHORT).show()

            // Navega al fragmento CardEditFragment
            // pasando el id de card como argumento
            it.findNavController()
                .navigate(CardListFragmentDirections.actionCardListFragmentToCardEditFragment(card.id))
        }

        return binding.root
    }
}