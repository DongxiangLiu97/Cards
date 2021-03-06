package es.uam.eps.dadm.cards

class Cloze(
        question: String,
        answer: String,

        ): Card( question, answer) {

    override fun show(){
            var flagInsert= false
            var cadena=""

            var listIndex= question.withIndex().filter { it.value=='*' }.map{ it.index}.toList()
            if (listIndex.size ==2 && (listIndex[1] - listIndex[0])>=2){
                for (c in question){
                    if (c == '*') {
                        if (!flagInsert) {
                            cadena += answer
                            flagInsert = true
                        } else flagInsert = false
                    }
                    else if(!flagInsert){
                            cadena+=c
                    }
                }
                print("$question (INTRO para ver respuesta)")
                readLine()

                print("$cadena (Teclea 0,3 o 5): ")
                val q= readLine()!!.toInt()
                if (q==0||q==3||q==5) {
                    quality= q
                } else {
                    throw Exception("Unavailable quality")
                }
            }else {
                throw Exception("Unavailable question")
            }
        
    }
    override fun toString() = "cloze  | $question | $answer | $date | $id | $easiness | $repetitions | $interval | $nextPracticeDate\n"

    companion object {
        fun fromString(cad: String): Cloze {
            val trozos = cad.split(" | ")
            val card = Cloze(trozos[1].trim(), trozos[2].trim())
            card.date= trozos[3].trim()
            card.id= trozos[4].trim()
            card.deckId=trozos[5].trim()
            card.easiness= trozos[6].trim().toDouble()
            card.repetitions= trozos[7].trim().toInt()
            card.interval= trozos[8].trim().toLong()
            card.nextPracticeDate= trozos[9].trim()
            return card
        }
    }
}