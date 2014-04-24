package app.parking;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

import parking.protocol.Protocol.PaymentMethod;
import app.parking.db.ParkingEntry;

public class UserAccount {

	// Properties
	private static String userName;
	private static float amount;
	private static List<ParkingEntry> entries = new ArrayList<ParkingEntry>();

	// Events
	private static OnAmountChangeListener mOnAmountChangeListener;

	private static void notifyAmountChange(float oldAmount, float newAmount) {
		if (mOnAmountChangeListener != null) {
			mOnAmountChangeListener.onAmountChange(oldAmount, newAmount);
		}
	}

	// Getters and Setters
	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		UserAccount.userName = userName;
	}

	public static float getAmount() {
		return amount;
	}

	public static void setAmount(float amount) {
		float oldAmount = UserAccount.amount;
		UserAccount.amount = amount;

		if (oldAmount != UserAccount.amount) {
			notifyAmountChange(oldAmount, UserAccount.amount);
		}
	}

	public static void setOnAmountChangedListener(
			OnAmountChangeListener onAmountChangeListener) {
		mOnAmountChangeListener = onAmountChangeListener;
	}

//	public static void registerEntry(int parkingID, int entryID,
//			Date entryTime, PaymentMethod paymentMethod, float parkingFee) {
//		entries.add(new ParkingEntry(parkingID, entryID, entryTime, parkingFee));
//	}
	
	public static void registerEntry(int parkingID, int entryID,
	Date entryTime, PaymentMethod paymentMethod, float parkingFee) {
entries.add(new ParkingEntry(parkingID, entryID, entryTime, paymentMethod, parkingFee));
}

	// Events
	public interface OnAmountChangeListener extends EventListener {
		void onAmountChange(float oldAmount, float newAmount);
	}
}
