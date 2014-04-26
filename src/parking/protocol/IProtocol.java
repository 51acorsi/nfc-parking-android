package parking.protocol;

public interface IProtocol {

/*Command Blocks
 * 
 * Special
 * 0xF0
 * 0xFF
 * 
 * Command:
 * 0x10
 * 0x2F
 * 
 * Name:
 * 0x30
 * 0x5F
 * 
 * Error Exceptions
 * 0x60
 * 0x9F 
 * 	  
 */
	
	//Special
	public final byte con_wrong_parameter = (byte) 0xFB;
	public final byte con_ok = (byte) 0xFC;
	public final byte con_unknown = (byte) 0xFD;
	public final byte con_start = (byte) 0xFE;
	public final byte con_end = (byte) 0xFF;
	
	//Commands
	public final byte con_cmd_ex  = 0x09;
	public final byte con_cmd_get = 0x10;
	public final byte con_cmd_set = 0x11;
	public final byte con_cmd_req = 0x12;
	
	//Names
	public final byte con_nam_uid = 0x30;
	public final byte con_nam_new_entry = 0x31;
	public final byte con_nam_payment = 0x32;
	
	//Error Exceptions
	public final byte con_ex_default = 0x60;
	public final byte con_ex_amount = 0x61;
	public final byte con_ex_entry_not_found = 0x62;
}
