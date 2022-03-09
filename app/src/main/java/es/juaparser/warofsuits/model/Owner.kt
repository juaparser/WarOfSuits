package es.juaparser.kanbanboard.model

import java.util.*

/*
* Modelo issue, almacenará la información de todos los issues del tablero kanban asociado a un repo
*   title: String con el título del issue
*   date: Date con la fecha de creación del issue
*   number: Integer con el identificador del issue
*   comments: Número de comentarios en el issue
*   type: Enumerado BoardState para identificar a cual columna del tablero corresponde el issue
* */

data class Owner(
    var login: String,
    var id: Int,
)
