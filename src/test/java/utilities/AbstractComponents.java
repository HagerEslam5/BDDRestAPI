package utilities;

import static org.hamcrest.Matchers.lessThan;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class AbstractComponents {
	public static RequestSpecification reqSpec;

	public JsonPath convResponseToJson(String response) {
		JsonPath js = new JsonPath(response);
		return js;
	}

	public RequestSpecification buildRequestSpec() throws IOException {
		if (reqSpec == null) {
			PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
			reqSpec = new RequestSpecBuilder().setBaseUri(getGlobalData("BaseUrl"))
					.addFilter(RequestLoggingFilter.logRequestTo(log))
					.addFilter(ResponseLoggingFilter.logResponseTo(log)).addHeader("Content-Type", "application/json")
					.addHeader("Accept", "application/json").build();
			return reqSpec;
		}
		return reqSpec;

	}

	public ResponseSpecification buildResponseSpec() {

		ResponseSpecification resSpec = new ResponseSpecBuilder().expectResponseTime(lessThan(20000L)).build();
		return resSpec;
	}

	public String getGlobalData(String key) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\test\\java\\utilities\\global.properties");
		prop.load(fis);
		return prop.getProperty(key);
	}
}
