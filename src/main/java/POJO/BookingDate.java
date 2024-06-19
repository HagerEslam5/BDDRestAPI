package POJO;

public class BookingDate {

	private String checkin;
	public String getCheckin() {
		return checkin;
	}
	
	public BookingDate() {
		this.checkin="2018-01-01";
		this.checkout="2019-01-01";
	}
	public void setCheckin(String checkin) {
		this.checkin = checkin;
	}
	public String getCheckout() {
		return checkout;
	}
	public void setCheckout(String checkout) {
		this.checkout = checkout;
	}
	private String checkout;
}
