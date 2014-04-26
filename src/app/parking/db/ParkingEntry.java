package app.parking.db;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import parking.protocol.Protocol.PaymentMethod;

public class ParkingEntry {

	public enum PaymentStatus {
		Pending, Paid;
	}

	private int parkingID;
	private String parkingName;
	private int entryID;
	private Date entryTime;
	private PaymentMethod paymentMethod;
	private float parkingFee;
	private PaymentStatus paymentStatus;

	public ParkingEntry() {

	}

	public ParkingEntry(int parkingID, String parkingName, int entryID, Date entryTime, PaymentMethod paymentMethod,
			float parkingFee) {
		this.parkingID = parkingID;
		this.parkingName = parkingName;
		this.entryID = entryID;
		this.entryTime = entryTime;
		this.paymentMethod = paymentMethod;
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
	
	public String getDisplayEntryTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault());
		return df.format(this.entryTime);
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public float getParkingFee() {
		return parkingFee;
	}

	public String getDisplayParkingFee()
	{
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
		return nf.format(this.parkingFee);
	}	
	
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
}
