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
import es.uam.eps.dadm.cards.databinding.FragmentStatisticsBinding


class StatisticsFragment : Fragment() {
    lateinit var binding: FragmentStatisticsBinding
    private val viewModel: StudyViewModel by lazy {
        ViewModelProvider(this).get(StudyViewModel::class.java)
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

        val bCart=binding.chart
        val numCards=viewModel.cards.size
        val numDecks=viewModel.decks.size



        val xAxisValues = ArrayList<String>()
        xAxisValues.add("Decks")
        xAxisValues.add("Cards")


        val yValue = ArrayList<BarEntry>()
        yValue.add(BarEntry(1f, numDecks.toFloat()))
        yValue.add(BarEntry(2f, numCards.toFloat()))
        var pos=2f

        viewModel.decks.forEach { deck ->
            pos+=1f
            xAxisValues.add(deck.name)
            yValue.add(BarEntry(pos,deck.cards.size.toFloat()))

            var intervalTotal=0L
            var intervalMax=0L
            var easinessTotal=0.0
            var easinessMax=0.0
            var repetitions=0
            deck.cards.forEach{
                easinessTotal += it.easiness
                if(it.easiness> easinessMax)
                    easinessMax=it.easiness
                repetitions+=it.repetitions
                intervalTotal += it.interval
                if(it.interval> intervalMax)
                    intervalMax=it.interval

            }
            xAxisValues.add(deck.name+" rep")
            pos+=1f
            yValue.add(BarEntry(pos,repetitions.toFloat()))
            xAxisValues.add(deck.name + " intMed")
            pos += 1f
            if(deck.cards.size!=0) {

                yValue.add(BarEntry(pos, (intervalTotal / (deck.cards.size).toLong()).toFloat()))

            }else{
                yValue.add(BarEntry(pos, 0f))
            }
            xAxisValues.add(deck.name+" intMax")
            pos+=1f
            yValue.add(BarEntry(pos,intervalMax.toFloat()))
            xAxisValues.add(deck.name+" easMed")
            pos+=1f
            if(deck.cards.size!=0) {
            yValue.add(BarEntry(pos,(easinessTotal/(deck.cards.size).toLong()).toFloat()))
            }else{
                yValue.add(BarEntry(pos, 0f))
            }
            xAxisValues.add(deck.name+" easMax")
            pos+=1f
            yValue.add(BarEntry(pos,easinessMax.toFloat()))
        }


        val barDataSet = BarDataSet(yValue, getString(R.string.numElement))
        barDataSet.setColors(Color.BLUE)

        barDataSet.setDrawIcons(false)
        val barData = BarData(barDataSet)


        val xAxis = bCart.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 9f

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        xAxis.labelCount = xAxisValues.size
        xAxis.mAxisMaximum = xAxisValues.size.toFloat()
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
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


        return binding.root
    }




}