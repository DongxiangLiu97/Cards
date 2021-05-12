package es.uam.eps.dadm.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import es.uam.eps.dadm.cards.databinding.ListItemCardBinding

class CardAdapter() : RecyclerView.Adapter<CardAdapter.CardHolder>() {
    lateinit var binding: ListItemCardBinding
    lateinit var deckId: String


    var data =  listOf<Card>()

    inner class CardHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var card: Card

        fun bind(card: Card) {
            this.card = card
            binding.card = card
        }

        init {
            binding.listItemQuestion.setOnClickListener {
                val id = card.id

                it.findNavController()
                    .navigate(CardListFragmentDirections
                        .actionCardListFragmentToCardEditFragment(id,deckId))
            }
            binding.dataExpandMore.setOnClickListener {
                binding.dataExpandMore.visibility=View.INVISIBLE
                binding.dataExpandLess.visibility=View.VISIBLE

                binding.listItemData1.visibility=View.VISIBLE
                binding.listItemData2.visibility=View.VISIBLE
            }
            binding.dataExpandLess.setOnClickListener {
                binding.dataExpandMore.visibility=View.VISIBLE
                binding.dataExpandLess.visibility=View.INVISIBLE
                binding.listItemData1.visibility=View.GONE
                binding.listItemData2.visibility=View.GONE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemCardBinding.inflate(layoutInflater, parent, false)
        return CardHolder(binding.root)
    }

    override fun getItemCount() = data.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(data[position])

    }
}