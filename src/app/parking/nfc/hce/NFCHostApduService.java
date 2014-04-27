package app.parking.nfc.hce;

import java.nio.ByteBuffer;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parking.protocol.IProtocol;
import parking.protocol.Protocol;
import parking.protocol.Protocol.PaymentMethod;
import parking.protocol.exception.ParkingEntryNotFound;
import parking.protocol.exception.ProtocolException;
import parking.protocol.exception.UserAmountExceeded;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;
import app.parking.UserAccount;

public class NFCHostApduService extends HostApduService implements IProtocol {

	private int messageCounter = 0;

	// private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
		if (selectAidApdu(apdu)) {
			Log.i("HCEDEMO", "Application selected");
			return new byte[] { 0x4f, 0x4b };
		} else {
			// Test to debit customer account
			//UserAccount.setAmount(UserAccount.getAmount() - 5);

			Log.i("HCEDEMO", "Received: " + new String(Hex.encodeHex(apdu)));
			return this.processCommandReturn(apdu);
		}
	}

	private byte[] processCommandReturn(byte[] dataIn) {
		if (dataIn.length < 4)
			return null;

		// Check command start
		if (dataIn[0] != con_start) {
			Log.i("HCEDEMO", "Wrong start command");
			return Protocol.getUnknownCommand();
		}

		// Check end command
		if (dataIn[dataIn.length - 1] != con_end) {
			Log.i("HCEDEMO", "Wrong end command");
			return Protocol.getUnknownCommand();
		}

		switch (dataIn[1]) {
		case con_cmd_get:
			return this.processGetCommand(dataIn);

		case con_cmd_set:
			return this.processSetCommand(dataIn);
			
		case con_cmd_req:
			return this.processReqCommand(dataIn);

		default:
			Log.i("HCEDEMO",
					"Unknow command: " + new String(Hex.encodeHex(dataIn)));
			return Protocol.getUnknownCommand();
		}
	}

	private byte[] processGetCommand(byte[] dataIn) {

		// Look for get command
		if (dataIn.length < 3) {
			Log.i("HCEDEMO", "No get command specified");
			return Protocol.getUnknownCommand();
		}

		switch (dataIn[2]) {
		case con_nam_uid:
			return this.getUserId();
		default:
			Log.i("HCEDEMO", "Unknow get command");
			return Protocol.getUnknownCommand();
		}
	}

	private byte[] processSetCommand(byte[] dataIn) {

		// Look for Set command
		if (dataIn.length < 3) {
			Log.i("HCEDEMO", "No set command specified");
			return Protocol.getUnknownCommand();
		}

		switch (dataIn[2]) {
		case con_nam_new_entry:
			return this.registerNewEntry(dataIn);
		default:
			Log.i("HCEDEMO", "Unknow set command");
			return Protocol.getUnknownCommand();
		}
	}
	
	private byte[] processReqCommand(byte[] dataIn) {

		// Look for Set command
		if (dataIn.length < 3) {
			Log.i("HCEDEMO", "No req command specified");
			return Protocol.getUnknownCommand();
		}

		switch (dataIn[2]) {
		case con_nam_payment:
			return this.requestPayment(dataIn);
		default:
			Log.i("HCEDEMO", "Unknow req command");
			return Protocol.getUnknownCommand();
		}
	}

	private byte[] getUserId() {
		return UserAccount.getUserName().getBytes();
	}

	private byte[] registerNewEntry(byte[] dataIn) {
		// Validate Entries
		if (dataIn.length != 46) {
			Log.i("HCEDEMO", "Wrong Command Parameter for regNewEntry");
			return Protocol.getWrongCommandParameter();
		}

		byte[] bParkingName = new byte[20];
		ByteBuffer bb = ByteBuffer.wrap(dataIn, 3, 43);
		
		//Retrieve Parking ID
		int parkingId = bb.getInt();
		
		//Retrieve Parking Name
		bb.get(bParkingName);
		String parkingName = new String(bParkingName);
		parkingName = parkingName.trim();
		
		//Retrieve Entry ID
		int entryID = bb.getInt();
		
		//Retrieve Entry Time
		Date entryTime = new Date(bb.getLong());
		
		//Retrieve Payment Method
		PaymentMethod paymentMethod = PaymentMethod.fromOrdinal(bb.getShort());
		
		//Retrieve Parking Fee
		float parkingFee = bb.getFloat();
		
		UserAccount.registerEntry(parkingId, parkingName, entryID, entryTime, paymentMethod, parkingFee);

		return Protocol.getConfirmCommand();
	}

	private byte[] requestPayment(byte[] dataIn) {
		// Validate Entries
		if (dataIn.length != 12) {
			Log.i("HCEDEMO", "Unknow Command Parameter for reqPayment");
			return Protocol.getWrongCommandParameter();
		}

		ByteBuffer bb = ByteBuffer.wrap(dataIn, 3, 9);
		
		//Retrieve Parking ID
		int parkingId = bb.getInt();
		
		//Retrieve Entry ID
		int entryId = bb.getInt();
		
		//Request Entry Payment
		try {
			UserAccount.payEntry(parkingId, entryId);
		} catch (ProtocolException e) {
			e.printStackTrace();
			return e.getErrorAPDU();
		}

		return Protocol.getConfirmCommand();
	}	
	
	private byte[] getWelcomeMessage() {
		return "Hello Desktop!".getBytes();
	}

	private byte[] getNextMessage() {
		return ("Message from S4: " + messageCounter++).getBytes();
	}

	private boolean selectAidApdu(byte[] apdu) {
		return apdu.length >= 2 && apdu[0] == (byte) 0
				&& apdu[1] == (byte) 0xa4;
	}

	@Override
	public void onDeactivated(int reason) {
		Log.i("HCEDEMO", "Deactivated: " + reason);
	}
}