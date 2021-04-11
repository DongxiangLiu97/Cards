package es.uam.eps.dadm.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import es.uam.eps.dadm.cards.databinding.ListItemDeckBinding

class DeckAdapter() : RecyclerView.Adapter<DeckAdapter.DeckHolder>() {
    lateinit var binding: ListItemDeckBinding


    var data =  listOf<Deck>()

    inner class DeckHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var deck: Deck

        fun bind(deck: Deck) {
            this.deck = deck
            binding.deck = deck
        }

        init {
            binding.listItemName.setOnClickListener {
                val id = deck.id
                it.findNavController()
                        .navigate(DeckListFragmentDirections
                                .actionDeckListFragmentToCardListFragment(id))
            }
            binding.listItemEditButton.setOnClickListener {
                val id = deck.id
                it.findNavController()
                        .navigate(DeckListFragmentDirections
                                .actionDeckListFragmentToDeckEditFragment(id))
            }

        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): DeckHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemDeckBinding.inflate(layoutInflater, parent, false)
        return DeckHolder(binding.root)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: DeckHolder, position: Int) {
        holder.bind(data[position])
    }
}