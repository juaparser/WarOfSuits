package es.juaparser.warofsuits.model

import es.juaparser.warofsuits.model.Card
import java.util.*

/*
* Modelo Repo, almacenará la información de todos los repositorios del usuario
*   name: String con el nombre del repositorio
*   author: String con el autor del repositorio
*   link: Enlace al repositorio en Github
*   created: Fecha de creación del repositorio
*   issues: Lista de issues del tablero kanban asociado al repositorio
* */


data class Player(
    var id: Int,
    var name: String,
    var playableCard: Card?,
    var deck: MutableList<Card>,
    var discardDeck: MutableList<Card>,
    )
