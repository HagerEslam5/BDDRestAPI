package utilities;

public enum APIResources {

	CreateToken("/auth"),
	CreateBooking("/booking"),
	GetBooking("/booking/{bookingID}"),
	PartialUpdateBooking("/booking/{bookingID}"),
	DeleteBooking("/booking/{bookingID}");

	private String resource;
	APIResources(String resource) {
		this.resource=resource;
	}


	public String getResource() {
		return resource;
	}
	
}
