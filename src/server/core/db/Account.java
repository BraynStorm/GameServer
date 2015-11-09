package server.core.db;

import java.sql.Date;
import java.util.List;

import server.game.entities.Player;
import server.network.Client;

public class Account {
    private int id;
    private List<ShellCharacter> characters;
    private Date registeredOn;
    
    private Player playerCharacter;
    private Client client;
    
    public Account(int id, List<ShellCharacter> characterList, Date registeredOn) {
        this.id = id;
        this.characters = characterList;
        this.registeredOn = registeredOn;
    }
    
    public void selectCharacter(int id){
        playerCharacter = new Player(client, characters.get(id));
        
    }
    
    public Player getPlayerCharacter(){
        return playerCharacter;
    }
    

    public boolean isLoggedIn() {
        return playerCharacter != null;
    }

    public List<ShellCharacter> getCharacterList() {
        return characters;
    }

}
