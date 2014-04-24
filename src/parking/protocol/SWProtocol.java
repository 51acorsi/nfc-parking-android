package parking.protocol;

public class SWProtocol extends Protocol {

	public static byte[] getUserIDCommand() {
		return new byte[] { con_start, con_cmd_get, con_nam_uid, con_end };
	}

}
