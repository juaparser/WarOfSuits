package es.juaparser.warofsuits.model

/*
* Modelo issue, almacenará la información de todos los issues del tablero kanban asociado a un repo
*   title: String con el título del issue
*   date: Date con la fecha de creación del issue
*   number: Integer con el identificador del issue
*   comments: Número de comentarios en el issue
*   type: Enumerado BoardState para identificar a cual columna del tablero corresponde el issue
* */

data class Card(
    var value: String,
    var suit: String
)


fun Card.compareWith(card: Card): Int {
    // 0 IF EQUAL, 1 IF GREATER, -1 IF LOWER, 2 IF NONE

    var res = 2

    val firstValue = this.value.all { Character.isDigit(it) }
    val secondValue = card.value.all { Character.isDigit(it) }

    when {
        firstValue && secondValue -> {
            // BOTH INTS
            res = this.value.toInt().compareTo(card.value.toInt())
        }
        !firstValue && secondValue -> {
            // THIS IS LETTER, IS GREATER
            res = 1
        }
        firstValue && !secondValue -> {
            // SECOND LETTER, NOT GREATER
            res = -1
        }
        !firstValue && !secondValue -> {
            // BOTH LETTERS, CHECK EQUALLY
            when(this.value) {
                "J" -> res = if(card.value == "J") 0 else -1
                "Q" -> res = if(card.value == "J") 1 else if (card.value == "Q") 0 else -1
                "K" -> res = if(card.value == "J" || card.value == "Q") 1 else if (card.value == "K") 0 else -1
                "A" -> res = if(card.value == "J" || card.value == "Q" || card.value == "K") 1 else if (card.value == "A") 0 else -1
            }
        }
    }
    return res
}
