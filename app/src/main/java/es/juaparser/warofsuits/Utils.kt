package es.juaparser.warofsuits

import android.util.Log
import es.juaparser.warofsuits.model.Card

/**
 * Se definen las principales colecciones que guardaran la información de la partida.
 *
 * suits: Listado con los palos existentes. Esta lista se barajará al inicio de cada partida para
 *          que la prioridad de cada palo sea distinta en cada partida.
 *
 * top: Listado auxiliar identificado los valores alfabéticos de las cartas.
 *
 * deck: Listado que contendrá la baraja completa del juego, la cual se genera y se baraja al inicio
 *      de cada partida.
 */

    val suits = mutableListOf("diamonds", "clubs", "hearts", "spades")
    val top = mutableListOf("J", "Q", "K", "A")

    val deck = mutableListOf<Card>()


/**
 * generateDeck(): Función para generar una nueva baraja al inicio de cada partida (nueva o reiniciar)
 */
    fun generateDeck() {
        for(s in suits) {
            for(i in 2..10) {
                deck.add(Card(i.toString(), s))
            }
            for(t in top) {
                deck.add(Card(t,s))
            }
        }
        Log.d("JPS", "Deck generated with ${deck.size} cards: " + deck)
    }


/**
 * compareSuit(card): Función para comparar por palos si el valor de las cartas son iguales.
 *                  Esta comparación vendra dada por el orden de los palos en la lista 'suits'.
 */
fun Card.compareSuit(card: Card): Boolean {
    return suits.indexOf(this.suit) < suits.indexOf(card.suit)
}

/**
 * getDrawable(): Función para obtener el identificador de la imágen que corresponde a cada carta.
 *
 *  OPCIÓN A MEJORA: En vez de hacer uno a uno la asociación, dar un nombre adecado a cada imagen
 *                   y descomponer su nombre para obtenerlo dinámicamente.
 */
fun Card.getDrawable(): Int {
    return when (this.suit) {
        "diamonds" -> {
            when (this.value) {
                "2" -> R.drawable.two_diamonds
                "3" -> R.drawable.three_diamonds
                "4" -> R.drawable.four_diamonds
                "5" -> R.drawable.five_diamonds
                "6" -> R.drawable.six_diamonds
                "7" -> R.drawable.seven_diamonds
                "8" -> R.drawable.eight_diamonds
                "9" -> R.drawable.nine_diamonds
                "10" -> R.drawable.ten_diamonds
                "J" -> R.drawable.j_diamonds
                "Q" -> R.drawable.q_diamonds
                "K" -> R.drawable.k_diamonds
                "A" -> R.drawable.a_diamonds
                else -> R.drawable.reverse1
            }
        }
        "clubs" -> {
            when (this.value) {
                "2" -> R.drawable.two_clubs
                "3" -> R.drawable.three_clubs
                "4" -> R.drawable.four_clubs
                "5" -> R.drawable.five_clubs
                "6" -> R.drawable.six_clubs
                "7" -> R.drawable.seven_clubs
                "8" -> R.drawable.eight_clubs
                "9" -> R.drawable.nine_clubs
                "10" -> R.drawable.ten_clubs
                "J" -> R.drawable.j_clubs
                "Q" -> R.drawable.q_clubs
                "K" -> R.drawable.k_clubs
                "A" -> R.drawable.a_clubs
                else -> R.drawable.reverse1
            }
        }
        "spades" -> {
            when (this.value) {
                "2" -> R.drawable.two_spades
                "3" -> R.drawable.three_spades
                "4" -> R.drawable.four_spades
                "5" -> R.drawable.five_spades
                "6" -> R.drawable.six_spades
                "7" -> R.drawable.seven_spades
                "8" -> R.drawable.eight_spades
                "9" -> R.drawable.nine_spades
                "10" -> R.drawable.ten_spades
                "J" -> R.drawable.j_spades
                "Q" -> R.drawable.q_spades
                "K" -> R.drawable.k_spades
                "A" -> R.drawable.a_spades
                else -> R.drawable.reverse1
            }
        }
        "hearts" -> {
            when (this.value) {
                "2" -> R.drawable.two_hearts
                "3" -> R.drawable.three_hearts
                "4" -> R.drawable.four_hearts
                "5" -> R.drawable.five_hearts
                "6" -> R.drawable.six_hearts
                "7" -> R.drawable.seven_hearts
                "8" -> R.drawable.eight_hearts
                "9" -> R.drawable.nine_hearts
                "10" -> R.drawable.ten_hearts
                "J" -> R.drawable.j_hearts
                "Q" -> R.drawable.q_hearts
                "K" -> R.drawable.k_hearts
                "A" -> R.drawable.a_hearts
                else -> R.drawable.reverse1
            }
        }
        else -> R.drawable.reverse1
    }
}


/**
 * compareWith(card): Función para comparar el valores entre dos cartas.
 *      El resultado será 0 si son iguales, 1 si la primera es mayor, -1 si menor y 2 en otro caso.
 *
 *      Para ello se intenta convertir el valor de cada carta a un número real, y se identifica cada
 *      caso comparandolos como Int.
 *
 *      En caso de que ambos no sean dígitos, se hace una comparación por cada valor alfabético respecto
 *      a los otros posibles.
 */
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

