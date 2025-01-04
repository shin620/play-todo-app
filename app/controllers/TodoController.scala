package controllers

import play.api.mvc._
import models.Todo

import javax.inject._
import scala.collection.mutable

@Singleton
class TodoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val todos: mutable.ListBuffer[Todo] = mutable.ListBuffer(
    Todo(1, "Learn Scala", completed = false),
    Todo(2, "Build a TODO app", completed = false)
  )

  def list() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.todoList(todos.toList))
  }

  def add() = Action { implicit request: Request[AnyContent] =>
    val title = request.body.asFormUrlEncoded
      .flatMap(_.get("title").flatMap(_.headOption))
      .getOrElse("")

    if (title.nonEmpty) {
      val id = if (todos.isEmpty) 1 else todos.map(_.id).max + 1
      todos += Todo(id, title, completed = false)
    }

    Redirect(routes.TodoController.list())
  }


  def remove(id: Int) = Action {
    val tmp = todos.filter(_.id == id)(0)
    todos --= todos.filter(_.id == id)
    todos += Todo(tmp.id, tmp.title, true)
    Redirect(routes.TodoController.list())
  }
}

