package es.uam.eps.dadm.cards


import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class CardSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    companion object {
        val MAXIMUM_KEY = "max_number_cards"
        val MAXIMUM_DEFAULT = "20"
        val BOARD_KEY="board"
        val BOARD_DEFAULT=false

        fun getMaximumNumberOfCards(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(MAXIMUM_KEY, MAXIMUM_DEFAULT)
        }
        fun setMaximumNumberOfCards(context: Context, max: String) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(SettingsActivity.MAXIMUM_KEY, max)
            editor.commit ()
        }
        fun isBoardVisible(context: Context)= PreferenceManager
            .getDefaultSharedPreferences(context)
            .getBoolean(BOARD_KEY,BOARD_DEFAULT)

    }
}