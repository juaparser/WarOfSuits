package es.juaparser.kanbanboard.model

/*
* Enumerado BoardState, el cual identificará a qué columna del tablero corresponde cada issue.
*   BACKLOG: Columna backlog, issues abiertas pero no empezadas.
*   NEXT: Columna next, issues marcadas para comenzar en la siguiente iteración.
*   DOING: Columna doing, issues en progreso.
*   DONE: Columna done, issues finalizadas y listas para pruebas.
* */

enum class BoardState {
    BACKLOG,
    NEXT,
    DOING,
    DONE
}