package es.uam.eps.dadm.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.uam.eps.dadm.cards.databinding.ActivityStudyBinding
import es.uam.eps.dadm.cards.databinding.FragmentStudyBinding
import es.uam.eps.dadm.cards.databinding.FragmentTitleBinding
import java.lang.Exception

class StudyFragment: Fragment() {
    lateinit var binding: FragmentStudyBinding
    private val viewModel: StudyViewModel by lazy {
        ViewModelProvider(this).get(StudyViewModel::class.java)
    }
    private var listener = View.OnClickListener { v ->
        // Asigana a quality el valor 0, 3 o 5,
        // dependiendo del botón que se haya pulsado
        val quality = when (v?.id) {
            R.id.easy_button-> 5
            R.id.doubt_button->3
            R.id.difficult_button->0
            else ->throw Exception("No valid quality")
        }
        // Llama al método update de viewModel
        viewModel.update(quality)

        // Si la propiedad card de viewModel es null
        // informa al usuario mediante un Toast de que
        // no quedan tarjetas
        if (viewModel.card == null){
            binding.relative.visibility= View.INVISIBLE
            Toast.makeText(activity,getString(R.string.no_cards), Toast.LENGTH_SHORT).show()
        }
        binding.invalidateAll()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            binding = DataBindingUtil.inflate<FragmentStudyBinding>(
            inflater,
            R.layout.fragment_study,
            container,
            false)
        binding.studyViewModel = viewModel
        binding.answerButton.setOnClickListener {
            viewModel.card?.answered = true
            binding.invalidateAll()
        }
        // Ajusta el escuchador listener a los botones de dificultad
        binding.easyButton.setOnClickListener(listener)
        binding.doubtButton.setOnClickListener(listener)
        binding.difficultButton.setOnClickListener(listener)


        return binding.root
    }












}