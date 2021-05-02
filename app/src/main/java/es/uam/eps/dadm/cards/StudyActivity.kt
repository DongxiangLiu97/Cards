package es.uam.eps.dadm.cards

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber
import es.uam.eps.dadm.cards.databinding.ActivityStudyBinding
import java.lang.Exception

class StudyActivity : AppCompatActivity() {


    lateinit var binding: ActivityStudyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_study)
        supportActionBar?.hide()

        Timber.i("onCreate called")

    }

}