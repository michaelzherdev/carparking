import java.util.Date

import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

import scala.util.{Failure, Success}

class RepositorySpec  extends PlaySpec with GuiceOneAppPerSuite with ScalaFutures {
  import models._
  import scala.concurrent.ExecutionContext.Implicits.global

  def dateIs(date: java.util.Date, str: String) = {
    new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str
  }

  // --

  def carRepo: CarRepository = app.injector.instanceOf(classOf[CarRepository])

  "Car model" should {

    "be retrieved by id" in {
      whenReady(carRepo.findById(2)) { car =>
        val c = car.get
        c.number must equal("d456ef")
        c.arrival must matchPattern {
          case date:java.util.Date if dateIs(date, "2018-02-07") =>
        }
      }
    }

    "be listed by being on parking" in {
      whenReady(carRepo.list()) { cars =>
        cars.total must equal(11)
        cars.items must have length(10)
      }
    }

    "be created" in {
      val car = new Car("n000ew", new Date(), Option(new Date()))
     carRepo.insert(car).onSuccess {
        case id =>
          whenReady(carRepo.findById(id.get)) { car =>
            car.get.number must equal("n000ew")
          }
      }
    }

    "be updated if needed" in {
      val result = carRepo.findById(1).flatMap { car =>
        car.get.departure = Option(new Date())
        carRepo.update(1, car.get).flatMap { _ =>
          carRepo.findById(1)
        }
      }
      whenReady(carRepo.findById(1)) { car =>
        val c = car.get
        c.number must equal("a123bc")
        c.arrival must matchPattern {
          case date:java.util.Date if dateIs(date, "2018-02-07") =>
        }
      }
    }
  }
}
