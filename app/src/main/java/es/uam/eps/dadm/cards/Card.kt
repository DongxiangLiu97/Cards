package es.uam.eps.dadm.cards

import android.view.View
import java.lang.Double.max
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

open class Card(
        var question: String,
        var answer: String,

        var date: String = LocalDateTime.now().toString(),
        var id: String = UUID.randomUUID().toString(),


        ){
    var quality=0
    var repetitions=0
    var interval=1L
    var nextPracticeDate= date
    var easiness= 2.5
    var answered=false

    open fun show() {
        print("$question (INTRO para ver respuesta)")
        var intro= readLine()
        print("$answer (Teclea 0,3 o 5): ")
        var q= readLine()!!.toInt()
        quality= q

    }
    fun update(currentDate: LocalDateTime){


        easiness=max(1.3, easiness + 0.1 - (5.0 - quality) * (0.08 + (5.0 - quality) * 0.02))

        if (quality < 3)
            repetitions= 0
        else
            repetitions += 1

        interval= if (repetitions<= 1) 1L
        else if (repetitions == 2) 6L
        else (easiness*interval).toLong()


        nextPracticeDate=currentDate.plusDays(interval).toString()

    }
    fun detail(){
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        println("eas = ${"%.2f".format(easiness)} rep = $repetitions int = $interval next=${nextPracticeDate.subSequence(0, 10)}")

    }



    override fun toString() = "card | $question | $answer | $date | $id | $easiness | $repetitions | $interval | $nextPracticeDate\n"

    companion object {
        fun fromString(cad: String): Card {
            var trozos = cad.split(" | ")
            var card = Card(trozos.get(1).trim(), trozos.get(2).trim(), trozos.get(3).trim(), trozos.get(4).trim())
            card.easiness=trozos.get(5).trim().toDouble()
            card.repetitions=trozos.get(6).trim().toInt()
            card.interval=trozos.get(7).trim().toLong()
            card.nextPracticeDate=trozos.get(8).trim()
            return card
        }
    }
    fun update_from_view(view: View) {
        quality = when(view.id) {
            R.id.easy_button -> 5
            R.id.doubt_button -> 3
            R.id.difficult_button -> 0
            else -> throw Exception("Unavailable quality")
        }
        update(LocalDateTime.now())
    }
    fun update_easy() {
        quality = 5
        update(LocalDateTime.now())
    }
    fun update_doubt(){
        quality = 3
        update(LocalDateTime.now())
    }
    fun update_difficult(){
        quality = 0
        update(LocalDateTime.now())
    }

    fun isDue(date: LocalDateTime): Boolean {

        val dateTime = LocalDateTime.parse(nextPracticeDate);
        return dateTime.isBefore(date) || dateTime.isEqual(date)


    }
}