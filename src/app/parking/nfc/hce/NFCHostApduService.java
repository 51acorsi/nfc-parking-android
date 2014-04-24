package app.parking.nfc.hce;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parking.protocol.IProtocol;
import parking.protocol.Protocol;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;
import app.parking.UserAccount;

public class NFCHostApduService extends HostApduService implements IProtocol{

	private int messageCounter = 0;
	//private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
		if (selectAidApdu(apdu)) {
			Log.i("HCEDEMO", "Application selected");
			return new byte[] {0x4f,0x4b};			
		}
		else {
			//Test to debit customer account 
			//UserAccount.setAmount(UserAccount.getAmount() - 5);
			
			Log.i("HCEDEMO", "Received: " + new String(apdu));
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
			
		default:
			Log.i("HCEDEMO", "Unknow command: " + Hex.encodeHexString(dataIn));
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
			return this.registerNewEntry();
		default:
			Log.i("HCEDEMO", "Unknow set command");
			return Protocol.getUnknownCommand();
		}
	}
	
	private byte[] getUserId()
	{
		return UserAccount.getUserName().getBytes();
	}
	
	private byte[] registerNewEntry()
	{
		
		
		return null;
	}
	
	private byte[] getWelcomeMessage() {
		return "Hello Desktop!".getBytes();
	}

	private byte[] getNextMessage() {
		return ("Message from S4: " + messageCounter++).getBytes();
	}

	private boolean selectAidApdu(byte[] apdu) {
		return apdu.length >= 2 && apdu[0] == (byte)0 && apdu[1] == (byte)0xa4;
	}

	@Override
	public void onDeactivated(int reason) {
		Log.i("HCEDEMO", "Deactivated: " + reason);
	}
}