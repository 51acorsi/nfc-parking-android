package parking.protocol.exception;

import parking.protocol.Protocol;

public class UserAmountExceeded extends ProtocolException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String msg;
	private final String defMsg = "Parking Entry not Found";

	public UserAmountExceeded ()
	{
		super();
		this.msg = defMsg;
	}
	
	
	@Override
	public byte[] getErrorAPDU() {
		return Protocol.getError(con_ex_amount, this.msg);
	}

}
