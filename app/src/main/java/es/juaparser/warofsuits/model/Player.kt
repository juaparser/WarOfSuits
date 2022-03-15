package es.juaparser.warofsuits.model

import es.juaparser.warofsuits.model.Card
import java.util.*

/**
 * Modelo player, almacenará la información de cada jugador
 *   id: Int para identificar de forma única a cada jugador.
 *   name: String que contiene el nombre del jugador.
 *   playableCard: Card que contiene la carta que el jugador tiene en juego en el momento. Si no está jugando ninguna, será null.
 *   deck: MutableList con las cartas que corresponde a cada jugador, previamente ya barajadas.
 *   discardDeck: MutableList con las cartas que va ganando el jugador.
 * */


data class Player(
    var id: Int,
    var name: String,
    var playableCard: Card?,
    var deck: MutableList<Card>,
    var discardDeck: MutableList<Card>,
    )
