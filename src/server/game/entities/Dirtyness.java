package server.game.entities;

import java.util.HashMap;
import java.util.Map;

public class Dirtyness {
	
	/*
     * XXX Well, it's probably better to have different 'dirty' flags.
     * DIRTY_MOTION,
     * DIRTY_POWERS,5
     * ... // Add new
     */
	
	public static final byte MOTION =        0;
	public static final byte POWERS = (byte) 1;
	
	private Map<Byte, Boolean> map;
	
	public Dirtyness() {
		map = new HashMap<>();
		map.put(MOTION, true);
		map.put(POWERS, true);
	}
	
	public void markDirty(byte type){
		map.put(type, true);
	}
	
	public void markClean(byte type){
		map.put(type, false);
	}
	
	public boolean getDirty(byte type){
		return map.get(type);
	}
}
