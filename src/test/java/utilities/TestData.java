package utilities;
import POJO.*;

public class TestData {
	Booking Booking = new Booking();

	public String createTokenDate(String username,String password) {
		return "{\r\n" + "    \"username\" : \""+username+"\",\r\n" + "    \"password\" : \""+password+"\"\r\n" + "}";
	}
	
	public String UpdateDataPartially(String fName, String lName) {
		Booking.setFirstname(fName);
		Booking.setLastname(lName);
		return "{\r\n"
				+ "    \"firstname\" : \""+Booking.getFirstname()+"\",\r\n"
				+ "    \"lastname\" : \""+Booking.getLastname()+"\"\r\n"
				+ "}";

	}

}