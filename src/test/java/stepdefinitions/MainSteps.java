package stepdefinitions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;

import org.testng.Assert;

import POJO.Booking;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.*;

public class MainSteps extends AbstractComponents {
	TestData TestData = new TestData();
	Booking Booking = new Booking();
	private RequestSpecification request;
	private Response resBody;
	private static String token;
	private static String bookingID;
	private String fName = "Micheal";
	private String lName = "Scott";

	@Given("CreateTokenAPI payload with {string} {string}")
	public void create_token_payload_with(String username, String admin) throws IOException {
		RequestSpecification reqSpec = buildRequestSpec();
		request = given().spec(reqSpec).body(TestData.createTokenDate(username, admin));
	}

	@Given("{string}API payload")
	public void payload(String api) throws IOException {
		RequestSpecification reqSpec = buildRequestSpec();
		if (api.equalsIgnoreCase("CreateBooking"))
			request = given().spec(reqSpec).body(Booking);
		else if (api.equalsIgnoreCase("PartialUpdateBooking"))
			request = given().spec(reqSpec).pathParam("bookingID", bookingID).header("Cookie", "token=" + token + "")
					.header("Accept", "application/json").body(TestData.UpdateDataPartially(fName, lName));
	}

	@Given("The BookingID and Access token")
	public void booking_id() throws IOException {
		RequestSpecification reqSpec = buildRequestSpec();
		request = given().spec(reqSpec).pathParam("bookingID", bookingID).header("Cookie", "token=" + token + "");

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
		Assert.assertTrue(resBody.getTime() < 3000);
	}


	@Then("{string} is successfully created")
	public void is_successfully_created(String string) {
		JsonPath js = convResponseToJson(resBody.asString());
		if (string.equalsIgnoreCase("bookingid"))
		bookingID = js.getString("bookingid");
		else if  (string.equalsIgnoreCase("token"))
		token = js.getString("token");
	}

	@Then("{string} in the response body is equal to the previously added value")
	public void in_the_response_body_is_equal_to_the_previously_added_value(String string) {
		JsonPath js = convResponseToJson(resBody.asString());
		Assert.assertEquals(js.getString("firstname"), Booking.getFirstname());
		Assert.assertEquals(js.getString("lastname"), Booking.getLastname());

	}

	@Then("data is updated successfully")
	public void data_is_updated_successfully() {
		resBody.then().assertThat().body("firstname", equalTo(fName)).assertThat().body("lastname", equalTo(lName));
	}

	@Then("verify data is deleted successfully using GetBooking API")
	public void data_is_deleted_successfully() {
		String deleteMess = resBody.then().extract().response().asString();
		Assert.assertEquals(deleteMess, "Created");
		APIResources resourceAPI = APIResources.valueOf("GetBooking");
		String getMess = request.when().get(resourceAPI.getResource()).then().extract().response().asString();
		Assert.assertEquals(getMess, "Not Found");
	}

}
