package server.network;

public class OPCodes {
	
	/**
	 * data:
	 * 	long: System.currentTimeMilli()) at the time of recieving this packet.
	 */
    public static final short PING = (short) 0x0001;
    
    
    /**
     * data:
     * 	byte emailLen
     *  byte passwordLen
     * 	String email
     *  String password
     */
    public static final short LOGIN_ATTEMPT = (short) 0x0002;
    
    
    /**
     * data:
     * 	byte status,
     * 	int entityID  // If the player logged in, they recieve their own ID which they use everytime doing anything.
     */
    public static final short LOGIN_STATUS = (short) 0x0003;
    
    
    /**
     * data:
     * 	byte status (0 - rejected, 1 - confirmed).
     */
    public static final short LAST_PACKET_STATUS = (short) 0x0004;
    
    
    /**
     * data:
     * 	int id,
     * 	byte isInMotion,
     * 	Vector3f position
     * 	Vector3f forward,
     * 	Vector3f up
     */
    public static final short ENTITY_MOTION_UPDATE = (short) 0x0005;
    
    
    /**
     * data:
     * 	int id,
	 *  Map<Ingeger, Item> itemList <slotID, itemData>
     */
    public static final short ENTITY_EQUIPMENT_UPDATE = (short) 0x0006;
    
    
    /**
     * data:
     * 	int id,
     * 	List<Power> powers
     * 		-- current, max, regenRate, name;
     */
    public static final short ENTITY_POWERS_UPDATE = (short) 0x0007;
    
    /**
     * data:
     * 	int id,
     * 	Spell spell
     *  int / Vector3f [targetID || areaPosition]
     */
    public static final short ENTITY_STARTED_CASTING_SPELL = (short) 0x0008;
    
    
    /**
     * data:
     * 	int id,
     * 	Spell spell,
     *  int targetID || Vector3f areaPosition,
     */
    public static final short ENTITY_FINISHED_CASTING_SPELL = (short) 0x0009;
    
    
    /**
     * data:
     * 	int id,
     * 	Item item.
     * 	int targetID || Vector3f areaPosition,
     */
    public static final short ENTITY_STARTED_USING_ITEM = (short) 0x0010;
    
    
    /**
     * data:
     * 	int id,
     * 	Item item,
     *  int targetID || Vector3f areaPosition,
     */
    public static final short ENTITY_FINISHED_USING_ITEM = (short) 0x0010;
}
