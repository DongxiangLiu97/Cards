package es.uam.eps.dadm.cards

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Deck(
    var name: String,
    var id: String=UUID.randomUUID().toString()
){
    val cards = mutableListOf<Card>()

    fun addCard(){
        println("Añadiendo tarjeta al mazo $name:")
        print("  Teclea el tipo (0 -> Card 1 -> Cloze):")
        val tipo= readLine()!!.toInt()
        print("  Teclea la pregunta:")
        val pregunta= readLine().toString()
        print("  Teclea la respuesta:")
        val respuesta= readLine().toString()
        if (tipo ==0)
            cards += listOf(Card(pregunta, respuesta))
        else{

            cards += listOf(Cloze(pregunta, respuesta))
        }
        println("  Tarjeta añadida correctamente")

    }
    fun listCards(){
        cards.forEach{ println("    ${it.question} -> ${it.answer}")}
    }
    fun simulate(period:Int){
        println("Simulación del mazo $name:")
        var now = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        for (i in 0..period){
            println("Fecha actual:"+ formato.format(now))
            cards.forEach{
                if( it.isDue(now)){
                    it.show()
                    it.update(now)
                    it.detail()
                }
            }
            now= now.plusDays(1)
        }
    }
    fun writeCards(name: String) {
        val file= File(name)
        file.bufferedWriter().use { out-> cards.forEach { out.write(it.toString()) } }

    }
    fun readCards(name: String) {
        val lines: List<String> = File(name).readLines()
        var type: String
        for (line in lines){
            type= line.split(" | ")[0]
            if (type=="card")
                cards+=Card.fromString(line)
            else
                cards+=Cloze.fromString(line)
        }


    }

}