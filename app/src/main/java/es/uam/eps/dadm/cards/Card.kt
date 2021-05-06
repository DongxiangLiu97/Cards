package es.uam.eps.dadm.cards


import java.lang.Double.max
import java.time.LocalDateTime
import java.util.*
import kotlin.math.roundToLong
import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey


@Entity(tableName = "cards_table")
open class Card(
        @ColumnInfo(name = "card_question")
        var question: String,
        var answer: String,
        var date: String = LocalDateTime.now().toString(),
        @PrimaryKey
        var id: String = UUID.randomUUID().toString(),

){
    lateinit var deckId:String
    var quality=0
    var repetitions=0
    var interval=1L
    var nextPracticeDate= date
    var easiness= 2.5
    var answered=false

    constructor() : this(
            "Pregunta",
            "Respuesta",
            LocalDateTime.now().toString(),
            UUID.randomUUID().toString()
    )

    open fun show() {
        print("$question (INTRO para ver respuesta)")
        readLine()
        print("$answer (Teclea 0,3 o 5): ")
        val q= readLine()!!.toInt()
        if (q==0||q==3||q==5)
            quality= q
        else
            throw Exception("Unavailable quality")
    }
    fun update(currentDate: LocalDateTime){


        easiness=max(1.3, easiness + 0.1 - (5.0 - quality) * (0.08 + (5.0 - quality) * 0.02))

        if (quality < 3)
            repetitions= 0
        else
            repetitions += 1

        interval= when {
            repetitions<= 1 -> 1L
            repetitions == 2 -> 6L
            else -> (easiness*interval).roundToLong()
        }


        nextPracticeDate=currentDate.plusDays(interval).toString()

    }
    fun detail(){

        println("eas = ${"%.2f".format(easiness)} rep = $repetitions int = $interval next=${nextPracticeDate.subSequence(0, 10)}")

    }



    override fun toString() = "card | $question | $answer | $date | $id | $easiness | $repetitions | $interval | $nextPracticeDate\n"

    companion object {
        fun fromString(cad: String): Card {
            val trozos = cad.split(" | ")
            val card = Card(trozos[1].trim(), trozos[2].trim(), trozos[3].trim(), trozos[4].trim())
            card.deckId=trozos[5].trim()
            card.easiness= trozos[6].trim().toDouble()
            card.repetitions= trozos[7].trim().toInt()
            card.interval= trozos[8].trim().toLong()
            card.nextPracticeDate= trozos[9].trim()
            return card
        }
    }

    fun isDue(date: LocalDateTime): Boolean {

        val dateTime = LocalDateTime.parse(nextPracticeDate)
        return dateTime.isBefore(date) || dateTime.isEqual(date)


    }
}