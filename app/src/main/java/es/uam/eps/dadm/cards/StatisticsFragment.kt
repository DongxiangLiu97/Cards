package es.uam.eps.dadm.cards

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import es.uam.eps.dadm.cards.databinding.FragmentStatisticsBinding
import timber.log.Timber


class StatisticsFragment : Fragment() {
    lateinit var binding: FragmentStatisticsBinding

    private val statisticsViewModel by lazy {
        ViewModelProvider(this).get(StatisticsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentStatisticsBinding>(
            inflater,
            R.layout.fragment_statistics,
            container,
            false
        )

        statisticsViewModel.loadDeckId(2)

        statisticsViewModel.deckWithCards.observe(viewLifecycleOwner) {
            val deck = it[0].deck
            val cards = it[0].cards
            Timber.i("El mazo " + deck.name + " tiene " + cards.size + " tarjetas")
        }
        return binding.root
    }





}