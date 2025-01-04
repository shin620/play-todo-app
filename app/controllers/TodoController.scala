package controllers

import play.api.mvc._
import models.Todo

import javax.inject._
import scala.collection.mutable

@Singleton
class TodoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val todos: mutable.ArrayBuffer[Todo] = mutable.ArrayBuffer(
    Todo("Learn Scala", completed = false),
    Todo("Build a TODO app", completed = false)
  )

  def list() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.todoList(todos))
  }

  def add() = Action { implicit request: Request[AnyContent] =>
    val title = request.body.asFormUrlEncoded
      .flatMap(_.get("title").flatMap(_.headOption))
      .getOrElse("")

    if title.nonEmpty
    then todos += Todo(title, completed = false)

    Redirect(routes.TodoController.list())
  }


  def switch(id: Int) = Action {
    val tmp = todos(id)
    todos(id) = Todo(tmp.title, !tmp.completed)
    Redirect(routes.TodoController.list())
  }
}

