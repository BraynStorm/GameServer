package server.game.items;

public class ItemDoesntExistException extends RuntimeException {
	int itemID;
	
	public ItemDoesntExistException(int id) {
		this.itemID = id;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5940246697148444765L;

}
