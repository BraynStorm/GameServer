package server.network;

public class OPCodes {
    public static final short PING            = (short) 0x0001;
    public static final short LOGIN_ATTEMPT   = (short) 0x0002;
    public static final short LOGIN_STATUS    = (short) 0x0003;
    public static final short LOGIN_TOKEN     = (short) 0x0004;
    public static final short ENTITY_UPDATE   = (short) 0x0005;
}
