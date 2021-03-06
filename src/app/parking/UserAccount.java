package app.parking;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

import parking.protocol.Protocol.PaymentMethod;
import parking.protocol.exception.ParkingEntryNotFound;
import parking.protocol.exception.UserAmountExceeded;
import app.parking.db.ParkingEntry;
import app.parking.db.ParkingEntry.PaymentStatus;

public class UserAccount {

	// Properties
	private static String userName;
	private static float amount;
	private static boolean autoPayment;
	private static List<ParkingEntry> entries = new ArrayList<ParkingEntry>();

	// Events
	private static OnAmountChangeListener mOnAmountChangeListener;
	private static OnAutoPaymentChangeListener mOnAutoPaymentChangeListener;
	private static OnEntryListChangeListener mOnEntryListChanged;

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
	
	public static String getDisplayAmount()
	{
		return new DecimalFormat("#.##").format(UserAccount.amount);
	}

	public static void setAmount(float amount) {
		if (UserAccount.amount != amount) {
			float oldAmount = UserAccount.amount;
			UserAccount.amount = amount;
			notifyAmountChange(oldAmount, UserAccount.amount);
		}
	}

	public static boolean getAutoPayment() {
		return UserAccount.autoPayment;
	}

	public static void setAutoPayment(boolean autoPayment) {
		if (UserAccount.autoPayment != autoPayment) {
			UserAccount.autoPayment = autoPayment;
			notifyAutoPaymentChange(UserAccount.autoPayment);
		}
	}

	public static List<ParkingEntry> getEntries() {
		return entries;
	}

	// Business Methods
	public static void registerEntry(int parkingID, String parkingName, int entryID, Date entryTime,
			PaymentMethod paymentMethod, float parkingFee) {
		entries.add(0, new ParkingEntry(parkingID, parkingName, entryID, entryTime, paymentMethod, parkingFee));
		notifyListChange();
	}

	public static void payEntry(int parkingId, int entryId) throws ParkingEntryNotFound, UserAmountExceeded {
		ParkingEntry entry = null;

		for (ParkingEntry pEntry : entries) {
			if (pEntry.getParkingID() == parkingId && pEntry.getEntryID() == entryId) {
				entry = pEntry;
				break;
			}
		}

		if (entry == null) {
			throw new ParkingEntryNotFound();
		}

		if (UserAccount.autoPayment) {
			// Pay Automatically
			if (entry.getPaymentStatus() == PaymentStatus.Pending) {
				UserAccount.pay(entry.getParkingFee());
				entry.setPaymentStatus(PaymentStatus.Paid);
				notifyListChange();
			}
		} else {

		}
	}

	private static void pay(float amount) throws UserAmountExceeded {

		if (UserAccount.amount < amount) {
			throw new UserAmountExceeded();
		}

		float oldAmount = UserAccount.amount;
		UserAccount.amount -= amount;
		notifyAmountChange(oldAmount, UserAccount.amount);
	}

	// Events Assignment
	public static void setOnAmountChangedListener(OnAmountChangeListener onAmountChangeListener) {
		mOnAmountChangeListener = onAmountChangeListener;
	}

	public static void setOnAutoPaymentChangedListener(OnAutoPaymentChangeListener onAutoPaymentChangeListener) {
		mOnAutoPaymentChangeListener = onAutoPaymentChangeListener;
	}

	public static void setOnEntryListChanged(OnEntryListChangeListener onEntryListChanged) {
		mOnEntryListChanged = onEntryListChanged;
	}

	// Event Interfaces
	public interface OnAmountChangeListener extends EventListener {
		void onAmountChange(float oldAmount, float newAmount);
	}

	public interface OnAutoPaymentChangeListener extends EventListener {
		void onStateChange(boolean newValue);
	}

	public interface OnEntryListChangeListener extends EventListener {
		void onListChange(List<ParkingEntry> parkingEntries);
	}

	// Event Triggers
	public static void forceNotification() {
		notifyAmountChange(UserAccount.amount, UserAccount.amount);
		notifyAutoPaymentChange(UserAccount.autoPayment);
		notifyListChange();
	}
	
	private static void notifyAmountChange(float oldAmount, float newAmount) {
		if (mOnAmountChangeListener != null) {
			mOnAmountChangeListener.onAmountChange(oldAmount, newAmount);
		}
	}

	private static void notifyAutoPaymentChange(boolean newValue) {
		if (mOnAutoPaymentChangeListener != null) {
			mOnAutoPaymentChangeListener.onStateChange(newValue);
		}
	}

	private static void notifyListChange() {
		if (mOnEntryListChanged != null) {
			mOnEntryListChanged.onListChange(UserAccount.entries);
		}
	}
}
