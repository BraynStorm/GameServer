package server.game.items;

public class EnchantmentDoesntExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3906636952059141295L;
	int id;
	
	public EnchantmentDoesntExistException(int id) {
		this.id = id;
	}
}
