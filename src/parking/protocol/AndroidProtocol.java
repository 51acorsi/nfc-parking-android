package parking.protocol;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;

public abstract class AndroidProtocol implements IProtocol {

	private Logger log;

	public void IdentifyCommand(byte[] dataIn) {
		if (dataIn.length < 4)
			return;

		// Check command start
		if (dataIn[0] != con_start) {
			log.info("Wrong start command");
			return; // Or Throw Error
		}

		// Check end command
		if (dataIn[dataIn.length - 1] != con_end) {
			log.info("Wrong end command");
			return; // Or Throw Error
		}

		switch (dataIn[1]) {
		case con_cmd_get:
			this.identifyGetCommand(dataIn);
			break;

		default:
			log.info("Unknow command: " + Hex.encodeHexString(dataIn));
			break;
		}

	}

	private void identifyGetCommand(byte[] dataIn) {

		// Look for get command
		if (dataIn.length < 3) {
			log.info("No get command specified");
			return; // Or Throw Error
		}

		switch (dataIn[2]) {
		case con_nam_uid:
			this.getUserId();
			break;
		default:
			log.info("Unknow get command");
			break;
		}
	}

	protected abstract void getUserId();

}
