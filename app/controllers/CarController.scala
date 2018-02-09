package controllers

import javax.inject.Inject

import models.{Car, CarRepository}
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import views._

import scala.concurrent.ExecutionContext

class CarController @Inject()(carRepository: CarRepository,
                              cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  private val logger = play.api.Logger(this.getClass)

  /**
    * This result directly redirect to the application home.
    */
  val Home = Redirect(routes.CarController.list(0, 2, ""))

  /**
    * Describe the car form (used in both edit and create screens).
    */
  val carForm = Form(
    mapping(
      "id" -> ignored(None: Option[Long]),
      "number" -> nonEmptyText,
      "arrival" -> date("dd.MM.yyyy HH:mm:ss"),
      "departure" -> optional(date("dd.MM.yyyy HH:mm:ss"))
    )(Car.apply)(Car.unapply)
  )

  // -- Actions

  /**
    * Handle default path requests, redirect to cars list
    */
  def index = Action {
    Home
  }

  /**
    * Display the paginated list of cars.
    *
    * @param page    Current page number (starts from 0)
    * @param orderBy Column to be sorted
    * @param filter  Filter applied on car names
    */
  def list(page: Int, orderBy: Int, filter: String) = Action.async { implicit request =>
    carRepository.list(page = page, orderBy = orderBy, filter = ("%" + filter + "%")).map { page =>
      Ok(html.list(page, orderBy, filter))
    }
  }

  /**
    * Display the 'edit form' of a existing Car.
    * @param id Id of the car to edit
    */
  def edit(id: Long) = Action.async { implicit request =>
    carRepository.findById(id).map {
      case Some(car) => Ok(html.editForm(id, carForm.fill(car)))
      case None => Ok("Not Found")
    }
  }

  /**
    * Handle the 'edit form' submission
    * @param id Id of the car to edit
    */
  def update(id: Long) = Action { implicit request =>
    carForm.bindFromRequest.fold(
      formWithErrors => {
        logger.error(s"form error: $formWithErrors")
        BadRequest(views.html.editForm(id, formWithErrors))
      },
      car => {
        carRepository.update(id, car)
        Home.flashing("success" -> "Car %s has been updated".format(car.number))
      }
    )
  }

  /**
    * Handle the 'new car form' submission.
    */
  def save = Action { implicit request =>
      carForm.bindFromRequest.fold(
        formWithErrors => {
          logger.error(s"form error: $formWithErrors")
          BadRequest(views.html.createForm(formWithErrors))
        },
        car => {
          carRepository.insert(car)
          Home.flashing("success" -> "Car %s has been created".format(car.number))
        }
      )
    }

  /**
    * Display the 'new car form'.
    */
  def create = Action { implicit request =>
      Ok(html.createForm(carForm.fill(null)))
    }

/**
  * Handle car deletion.
  */
def delete (id: Long) = Action.async {
  carRepository.delete(id).map { _ => Home.flashing ("success" -> "Car has been deleted") }
}

}