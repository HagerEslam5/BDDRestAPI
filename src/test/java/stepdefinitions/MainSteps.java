package stepdefinitions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.util.List;

import org.testng.Assert;

import POJO.Booking;
import POJO.BookingDate;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.*;

public class MainSteps extends AbstractComponents {
	TestData TestData = new TestData();
	private RequestSpecification request;
	private Response resBody;
	private static String token;
	private static int bookingID;
	private String fName = "Micheal";
	private String lName = "Scott";

	@Given("user credentials")
	public void user_credentials(List<String> data) throws IOException {
		RequestSpecification reqSpec = buildRequestSpec();
		request = given().spec(reqSpec).body(TestData.createTokenDate(data.get(0), data.get(1)));
	}

	@Given("{string}API payload")
	public void payload(String api) throws IOException {
		RequestSpecification reqSpec = buildRequestSpec();
		if (api.equalsIgnoreCase("CreateBooking")) {
			Booking Booking = new Booking();
			Booking.setFirstname("Jim");
			Booking.setLastname("Brown");
			Booking.setTotalprice("111");
			Booking.setDepositpaid(true);
			Booking.setAdditionalneeds("Breakfast");
			BookingDate BookingDate = new BookingDate();
			BookingDate.setCheckin("2018-01-01");
			BookingDate.setCheckout("2019-01-01");
			Booking.setBookingdates(BookingDate);
			request = given().spec(reqSpec).body(Booking);
		} else if (api.equalsIgnoreCase("PartialUpdateBooking")) {
			request = given().spec(reqSpec).pathParam("bookingID", bookingID).header("Cookie", "token=" + token)
					.header("Accept", "application/json").body(TestData.UpdateDataPartially(fName, lName));
		}
	}

	@Given("The BookingID and Access token")
	public void booking_id() throws IOException {
		RequestSpecification reqSpec = buildRequestSpec();
		request = given().spec(reqSpec).pathParam("bookingID", bookingID).header("Cookie", "token=" + token);
	}

	@When("User calls {string} API with {string} http request")
	public void user_calls_api_with_http_request(String resource, String method) {
		APIResources resourceAPI = APIResources.valueOf(resource);
		if (method.equalsIgnoreCase("POST"))
			resBody = request.when().post(resourceAPI.getResource());
		else if (method.equalsIgnoreCase("GET"))
			resBody = request.when().get(resourceAPI.getResource());
		else if (method.equalsIgnoreCase("PATCH"))
			resBody = request.when().patch(resourceAPI.getResource());
		else if (method.equalsIgnoreCase("DELETE"))
			resBody = request.when().delete(resourceAPI.getResource());
	}

	@Then("The API call is success with status code {int} and response time less than 3000L")
	public void the_api_call_is_success_with_status_code_and_response_time_less_than_3000l(Integer expectedStatusCode) {
		Assert.assertEquals(resBody.getStatusCode(), expectedStatusCode);
	}

	@Then("{string} is successfully created")
	public void is_successfully_created(String string) {
		JsonPath js = convResponseToJson(resBody.asString());
		if (string.equalsIgnoreCase("bookingid")) {
			resBody.then().body("bookingid", is(notNullValue()));
			bookingID = js.getInt("bookingid");
		} else if (string.equalsIgnoreCase("token")) {
			resBody.then().body("token", is(notNullValue()));
			token = js.getString("token");
		}
	}

	@Then("The data in the response body is the same as the previously added data")
	public void in_the_response_body_is_equal_to_the_previously_added_value() {
		POJO.Booking POJORes = resBody.then().extract().as(Booking.class);
		JsonPath js = convResponseToJson(resBody.asString());

		Assert.assertEquals(js.getString("firstname"), POJORes.getFirstname());
		Assert.assertEquals(js.getString("lastname"), POJORes.getLastname());
		Assert.assertEquals(js.getString("totalprice"), POJORes.getTotalprice());
		Assert.assertEquals(js.getBoolean("depositpaid"), POJORes.getDepositpaid());
		Assert.assertEquals(js.getString("additionalneeds"), POJORes.getAdditionalneeds());
		Assert.assertEquals(js.getString("bookingdates.checkin"), POJORes.getBookingdates().getCheckin());
		Assert.assertEquals(js.getString("bookingdates.checkout"), POJORes.getBookingdates().getCheckout());

	}

	@Then("data is updated successfully")
	public void data_is_updated_successfully() {
		resBody.then().assertThat().body("firstname", equalTo(fName)).assertThat().body("lastname", equalTo(lName));
	}

	@Then("Data is deleted successfully")
	public void data_is_deleted_successfully() {
		String deleteMess = resBody.then().extract().response().asString();
		Assert.assertEquals(deleteMess, "Created");
		APIResources resourceAPI = APIResources.valueOf("GetBooking");
		String getMess = request.when().get(resourceAPI.getResource()).then().extract().response().asString();
		Assert.assertEquals(getMess, "Not Found");
	}

}
