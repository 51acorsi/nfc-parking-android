package app.parking.db;

import java.util.Date;

public class ParkingEntry {

	public enum PaymentStatus {
		Pending, Paid;
	}

	private int parkingID;
	private String parkingName;
	private int entryID;
	private Date entryTime;
	//private PaymentMethod paymentMethod;
	private float parkingFee;
	private PaymentStatus paymentStatus;

	public ParkingEntry() {

	}

	public ParkingEntry(int parkingID, int entryID, Date entryTime,
			float parkingFee) {
		this.parkingID = parkingID;
		this.parkingName = "ParkingTest";
		this.entryID = entryID;
		this.entryTime = entryTime;
		// this.paymentMethod = paymentMethod;
		this.parkingFee = parkingFee;
		this.paymentStatus = PaymentStatus.Pending;
	}

	public int getParkingID() {
		return parkingID;
	}

	public String getParkingName() {
		return parkingName;
	}

	public int getEntryID() {
		return entryID;
	}

	public Date getEntryTime() {
		return entryTime;
	}

//	public PaymentMethod getPaymentMethod() {
//	return paymentMethod;
//}//	public static void registerEntry(int parkingID, int entryID,
//	Date entryTime, PaymentMethod paymentMethod, float parkingFee) {
//entries.add(new ParkingEntry(parkingID, entryID, entryTime, parkingFee));
	public float getParkingFee() {
		return parkingFee;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
}
