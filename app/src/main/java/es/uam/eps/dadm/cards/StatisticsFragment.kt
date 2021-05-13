package es.uam.eps.dadm.cards

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cards.database.DeckWithCards
import es.uam.eps.dadm.cards.databinding.FragmentStatisticsBinding
import java.time.LocalDateTime


class StatisticsFragment : Fragment() {
    lateinit var binding: FragmentStatisticsBinding
    private lateinit var user: FirebaseUser


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
        user = Firebase.auth.currentUser
        statisticsViewModel.loadUserId("ingles")
        statisticsViewModel.loadUserId(user.uid)

        statisticsViewModel.decksCards.observe(viewLifecycleOwner) {
            val bCart=binding.chart
            val cards= getCards(it)
            val decks= getDecks(it)
            val numCards= cards.size
            val numDecks = decks.size
            val xAxisValues = ArrayList<String>()
            xAxisValues.add("Decks")
            xAxisValues.add("Cards")
            var pos=1f
            val yValue = ArrayList<BarEntry>()
            yValue.add(BarEntry(pos, numDecks.toFloat()))
            pos+=1f
            yValue.add(BarEntry(pos, numCards.toFloat()))
            pos+=1f

            var intervalTotal=0L
            var intervalMax=0L
            var easinessTotal=0.0
            var easinessMax=0.0
            var repetitions=0
            cards.forEach{
                easinessTotal += it.easiness
                if(it.easiness> easinessMax)
                    easinessMax=it.easiness
                repetitions+=it.repetitions
                intervalTotal += it.interval
                if(it.interval> intervalMax)
                    intervalMax=it.interval

            }
            xAxisValues.add("Total repetitions")
            yValue.add(BarEntry(pos,repetitions.toFloat()))
            pos+=1f
            xAxisValues.add("AVG interval")

            if(cards.isNotEmpty()) {
                yValue.add(BarEntry(pos, (intervalTotal / (cards.size).toLong()).toFloat()))
            }else{
                yValue.add(BarEntry(pos, 0f))
            }
            pos += 1f
            xAxisValues.add("Max interval")
            yValue.add(BarEntry(pos,intervalMax.toFloat()))
            pos+=1f
            xAxisValues.add("AVG easiness")
            if(cards.isNotEmpty()) {
                yValue.add(BarEntry(pos,(easinessTotal/(cards.size).toLong()).toFloat()))
            }else{
                yValue.add(BarEntry(pos, 0f))
            }
            pos+=1f
            xAxisValues.add("Max easiness")
            yValue.add(BarEntry(pos,easinessMax.toFloat()))
            pos+=1f
            xAxisValues.add("Cards for next day")
            yValue.add(BarEntry(pos,cardNextDay(cards)))
            pos+=1f



            val barDataSet = BarDataSet(yValue, getString(R.string.numElement))
            barDataSet.setColors(Color.BLUE)

            barDataSet.setDrawIcons(false)
            val barData = BarData(barDataSet)


            val xAxis = bCart.xAxis
            xAxis.granularity = 1f

            xAxis.isGranularityEnabled = true
            xAxis.setCenterAxisLabels(true)
            xAxis.setDrawGridLines(false)
            xAxis.textSize = 10f

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

            xAxis.labelCount = xAxisValues.size
            xAxis.mAxisMaximum = xAxisValues.size.toFloat()
            xAxis.setCenterAxisLabels(true)
            //xAxis.setAvoidFirstLastClipping(true)
            xAxis.spaceMin = 1f
            xAxis.spaceMax = 1f
            xAxis.labelRotationAngle=90f

            bCart.setVisibleXRangeMaximum(12f)
            bCart.setVisibleXRangeMinimum(12f)
            bCart.isDragEnabled = true

            //Y-axis
            bCart.axisRight.isEnabled = false
            bCart.setScaleEnabled(true)

            val leftAxis = bCart.axisLeft
            leftAxis.valueFormatter = LargeValueFormatter()
            leftAxis.setDrawGridLines(false)
            leftAxis.spaceTop = 1f
            leftAxis.axisMinimum = 0f

            val description: Description=bCart.description
            description.text = ""
            bCart.data = barData
            bCart.setVisibleXRange(1f, 6f)
            bCart.animateY(5000)

        }



        return binding.root
    }

    private fun cardNextDay(cards: List<Card>)=
        cards.filter { card -> card.isDue(LocalDateTime.now()) }.size.toFloat()

    private fun getDecks(decksWithCardsList: List<DeckWithCards>): List<Deck> {
        val decksList: MutableList<Deck> = mutableListOf()
        decksWithCardsList.forEach {
            decksList += it.deck
        }
        return decksList
    }
    private fun getCards(decksWithCardsList: List<DeckWithCards>): List<Card> {
        val cardsList: MutableList<Card> = mutableListOf()
        decksWithCardsList.forEach {
            cardsList += it.cards
        }
        return cardsList
    }
}