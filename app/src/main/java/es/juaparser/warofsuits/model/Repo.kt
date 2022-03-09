package es.juaparser.kanbanboard.model

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

/*

      "id":346439465,
      "name":"DressMe",
      "owner":{
         "login":"juaparser",
         "id":32496382,
      },
      "issue_events_url":"https://api.github.com/repos/juaparser/DressMe/issues/events{/number}",
      "events_url":"https://api.github.com/repos/juaparser/DressMe/events",
      "issue_comment_url":"https://api.github.com/repos/juaparser/DressMe/issues/comments{/number}",
      "issues_url":"https://api.github.com/repos/juaparser/DressMe/issues{/number}",
      "created_at":"2021-03-10T17:34:53Z",
      "has_issues":true,
      "has_projects":true,
      "open_issues_count":0,
      "visibility":"public",
      "open_issues":0,
   },


 */
data class Repo(
    var id: Int,
    var name: String,
    var owner: Owner,
    var url: String,
    var created: Date,
    var issues: MutableList<Card>
    )
