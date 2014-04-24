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
 * Variables>
 * 0x60
 * 0x9F 
 * 	  
 */
	
	//Special
	public final byte con_ok = (byte) 0xFC;
	public final byte con_unknown = (byte) 0xFD;
	public final byte con_start = (byte) 0xFE;
	public final byte con_end = (byte) 0xFF;
	
	//Commands
	public final byte con_cmd_get = 0x10;
	public final byte con_cmd_set = 0x11;
	
	//Names
	public final byte con_nam_uid = 0x30;
	public final byte con_nam_new_entry = 0x31;
	
	//Variables
	

}
