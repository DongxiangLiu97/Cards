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

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.i("onSaveInstanceState called")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.i("onRestoreInstanceState called")
    }


    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy called")
    }
}