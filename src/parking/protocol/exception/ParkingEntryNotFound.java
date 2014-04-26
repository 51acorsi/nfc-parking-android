package parking.protocol.exception;

import parking.protocol.Protocol;

public class ParkingEntryNotFound extends ProtocolException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParkingEntryNotFound ()
	{
		super();
		this.msg = defMsg;
	}
	
	@Override
	public byte[] getErrorAPDU()
	{
		return Protocol.getError(con_ex_entry_not_found, this.msg);	
	}

}
