package parking.protocol.exception;

import parking.protocol.IProtocol;
import parking.protocol.Protocol;

public class ProtocolException extends Exception implements IProtocol{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String msg;
	protected final String defMsg = "Protocol Exception Thrown";
	
	public ProtocolException ()
	{
		super();
		this.msg = defMsg;
	}
	
	public ProtocolException (String msg)
	{
		this.msg = msg;
	}
	
	public byte[] getErrorAPDU()
	{
		return Protocol.getError(con_ex_default, this.msg);
	}

}
