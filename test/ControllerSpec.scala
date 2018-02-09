
import controllers.CarController
import org.scalatest.concurrent.ScalaFutures
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.CSRFTokenHelper._

class ControllerSpec extends PlaySpec with GuiceOneAppPerSuite with ScalaFutures {

  def dateIs(date: java.util.Date, str: String) = {
    new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str
  }

  def carController = app.injector.instanceOf(classOf[CarController])

  "CarController" should {

    "redirect to the car list on /" in {
      val result = carController.index(FakeRequest())
      status(result) must equal(SEE_OTHER)
      redirectLocation(result) mustBe Some("/cars")
    }

    "list cars on the the first page" in {
      val result = carController.list(0, 2, "")(FakeRequest())
      status(result) must equal(OK)
      contentAsString(result) must include("11 cars on parking")
    }

    "filter car by name" in {
      val result = carController.list(0, 2, "123")(FakeRequest())
      status(result) must equal(OK)
      contentAsString(result) must include("One car on parking")
    }

    "create new car" in {
      val badResult = carController.save(FakeRequest().withCSRFToken)
      status(badResult) must equal(BAD_REQUEST)
      val badDateFormat = carController.save(
        FakeRequest().withFormUrlEncodedBody("number" -> "FooBar").withCSRFToken
      )

      status(badDateFormat) must equal(BAD_REQUEST)
      contentAsString(badDateFormat) must include("""<input type="text" id="number" name="number" value="FooBar" """)


      val result = carController.save(
        FakeRequest().withFormUrlEncodedBody("number" -> "b345de", "arrival" -> "09.02.2018 15:06:53").withCSRFToken
      )

      status(result) must equal(SEE_OTHER)
      redirectLocation(result) mustBe Some("/cars")
      flash(result).get("success") mustBe Some("Car b345de has been created")
    }
  }
}
