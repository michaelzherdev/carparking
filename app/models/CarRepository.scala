package models

import java.util.Date
import javax.inject.Inject

import play.api.db.DBApi
import anorm.SqlParser._
import anorm._

import scala.concurrent.Future

case class Car(val id: Option[Long] = None, val number: String, val arrival: Date, var departure: Option[Date]) {
  def this(id: Option[Long], number: String, arrival: Date) {
    this(id, number, arrival, null)
  }
  def this(number: String, arrival: Date, departure: Option[Date]) {
    this(null, number, arrival, departure)
  }
}

/**
  * Helper for pagination.
  */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}


@javax.inject.Singleton
class CarRepository @Inject()(dbapi: DBApi)(implicit execContext: DatabaseExecutionContext)  {

  private val db = dbapi.database("default")

  private val simple = {
      get[Option[Long]]("car.id") ~
      get[String]("car.number") ~
      get[Date]("car.arrival") ~
      get[Option[Date]]("car.departure") map {
      case id~number~arrival~departure =>
        Car(id, number, arrival, departure)
    }
  }

  /**
    * Retrieve a car from the id.
    */
  def findById(id: Long): Future[Option[Car]] = Future {
    db.withConnection { implicit connection =>
      SQL("select * from car where id = {id}").on('id -> id).as(simple.singleOpt)
    }
  }(execContext)

  /**
    * Return a page of Car.
    * @param page Page to display
    * @param pageSize Number of cars per page
    * @param orderBy Car property used for sorting
    * @param filter Filter applied on the name column
    */
  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Future[Page[(Car)]] = Future {

    val offest = pageSize * page

    db.withConnection { implicit connection =>

      val cars = SQL(
        """
          select id, arrival, departure, number from car
          where car.number like {filter}
          and car.departure is null
          order by {orderBy} desc
          limit {pageSize} offset {offset}
        """
      ).on(
        'pageSize -> pageSize,
        'offset -> offest,
        'filter -> filter,
        'orderBy -> orderBy
      ).as(simple.*)

      val totalRows = SQL(
        """
          select count(*) from car
          where car.number like {filter}
          and car.departure is null
        """
      ).on(
        'filter -> filter
      ).as(scalar[Long].single)

      Page(cars, page, offest, totalRows)

    }

  }(execContext)

  /**
    * Update a car.
    * @param id The car id
    * @param car The car values.
    */
  def update(id: Long, car: Car) = Future {
    db.withConnection { implicit connection =>
      SQL(
        """
          update car
          set number = {number}, arrival = {arrival}, departure = {departure}
          where id = {id}
        """
      ).on(
        'id -> id,
        'number -> car.number,
        'arrival -> car.arrival,
        'departure -> car.departure
      ).executeUpdate()
    }
  }(execContext)

  /**
    * Insert a new car.
    * @param car The car values.
    */
  def insert(car: Car) = Future {
    db.withConnection { implicit connection =>
      SQL(
        """
          insert into car values (
            (select next value for car_seq),
            {number}, {arrival}, {departure}
          )
        """
      ).on(
        'number -> car.number,
        'arrival -> car.arrival,
        'departure -> car.departure
      ).executeInsert()
    }
  }(execContext)

  /**
    * Delete a car.
    * @param id Id of the car to delete.
    */
  def delete(id: Long) = Future {
    db.withConnection { implicit connection =>
      SQL("delete from car where id = {id}").on('id -> id).executeUpdate()
    }
  }(execContext)
}
