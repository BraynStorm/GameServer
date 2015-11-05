package server.game;

public class Zone {
	private int id;
	private World world;
	
    public Zone(int id, World world) {
        this.id = id;
        this.world = world;
    }

    public int getID() {
        return id;
    }
}
