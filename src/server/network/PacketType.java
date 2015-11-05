package server.network;

/**
 * Note char == ubyte.
 * @author BraynStorm
 *
 */
public class PacketType {
	
	/**
	 * data:<br>
	 * 	long: System.currentTimeMilli()) at the time of recieving this packet.
	 */
    public static final short PING = (short) 0x0001;
    
    
    /**
     * data:<br>
     * 	ubyte emailLen<br>
     * 	String email<br>
     *  char[60] password // bcrypt FTW <br>
     */
    public static final short LOGIN_ATTEMPT = (short) 0x00_02;
    
    
    /**
     * data:<br>
     * 	ubyte status,<br>
     *      // 0 - FAILED_UNKNOWN<br>
     *      1 - Successful + charList<br>
     *      2 - accSuspended<br>
     *      3 - accDoesn'tExist <br>
     *  
     *  if(status == 1){
     *      ubyte charListLength<br>
     *  
     * 	    List<Character> characters<br>
     *          -- int raceData,<br>
     *          -- ubyte nameLen,<br>
     *          -- ubyte zoneNameLen,<br>
     *          -- char[nameLen] name,<br>
     *          -- char[zoneNameLen] zoneName<br>
     *  } else if (status == 2){
     *      long suspensionLeft (seconds / m).
     *  }
     *  
     *  
     */
    public static final short LOGIN_STATUS = (short) 0x00_03;
    
    
    /**
     * data:<br>
     * 	ubyte status (0 - rejected, 1 - confirmed).
     */
    public static final short LAST_PACKET_STATUS = (short) 0x00_04;
    
    
    /**
     * data:<br>
     * 	int id,<br>
     * 	byte isInMotion,<br>
     * 	Vector3f position,<br>
     * 	Vector3f forward,<br>
     * 	Vector3f up
     */
    public static final short ENTITY_MOTION_UPDATE = (short) 0x00_05;
    
    
    /**
     * data:<br>
     * 	int id,<br>
	 *  Map<Ingeger, Item> itemList <slotID, itemData><br>
     */
    public static final short ENTITY_EQUIPMENT_UPDATE = (short) 0x00_06;
    
    
    /**
     * data:<br>
     * 	int id,<br>
     * 	List<Power> powers<br>
     * 		-- current, max, regenRate, name;<br>
     */
    public static final short ENTITY_POWERS_UPDATE = (short) 0x00_07;
    
    /**
     * data:<br>
     * 	int id,<br>
     * 	Spell spell<br>
     *  int / Vector3f [targetID || areaPosition]<br>
     */
    public static final short ENTITY_STARTED_CASTING_SPELL = (short) 0x00_08;
    
    
    /**
     * data:<br>
     * 	int id,<br>
     * 	Spell spell,<br>
     *  int targetID || Vector3f areaPosition,<br>
     */
    public static final short ENTITY_FINISHED_CASTING_SPELL = (short) 0x00_09;
    
    
    /**
     * data:<br>
     * 	int id,<br>
     * 	Item item.<br>
     * 	int targetID || Vector3f areaPosition,<br>
     */
    public static final short ENTITY_STARTED_USING_ITEM = (short) 0x00_10;
    
    
    /**
     * data:<br>
     * 	int id,<br>
     * 	Item item,<br>
     *  int targetID || Vector3f areaPosition,<br>
     */
    public static final short ENTITY_FINISHED_USING_ITEM = (short) 0x00_11;
}
