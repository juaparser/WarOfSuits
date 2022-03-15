package es.juaparser.warofsuits.model

/**
* Modelo card, almacenará la información de cada tarjeta del deck
*   value: String con el valor de la carta, tanto numérico como los valores alfabéticos
*   suit: String con el valor del palo de la carta.
* */

data class Card(
    var value: String,
    var suit: String
)