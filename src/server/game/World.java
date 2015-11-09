package server.game;

import java.util.ArrayList;
import java.util.List;

import braynstorm.commonlib.math.Vector3f;
import server.game.entities.EntityLiving;
import server.game.entities.Player;
import server.game.spells.Shape;
import server.network.Packet;

public class World {

    private List<Zone> zones;
    private List<EntityLiving> npcs;
    private List<Player> playerCharacters;
    
    public List<EntityLiving> getAllEntitiesInShape(Shape shape){
        // TODO make shapes like circle , sphere, square, box, 'path'
        
        List<EntityLiving> resultList = new ArrayList<>();
        resultList.addAll(getPlayersInShape(shape));
        resultList.addAll(getNPCsInShape(shape));
        
        return resultList;
    }
    
    public List<EntityLiving> getNPCsInShape(Shape shape){
        List<EntityLiving> resultList = new ArrayList<>();
        
        npcs.forEach(npc ->{
            if(shape.isPointInShape(npc.getPosition()))
                resultList.add(npc);
        });
        
        return resultList;
    }
    
    public List<Player> getPlayersInShape(Shape shape){
        List<Player> resultList = new ArrayList<>();
        
        playerCharacters.forEach(playerCharacter -> {
            if(shape.isPointInShape(playerCharacter.getPosition()))
                resultList.add(playerCharacter);
        });
        
        return resultList;
    }
    
    public void sendPacketToPlayersInShape(Packet packet, Shape shape){
        List<Player> players = getPlayersInShape(shape);
        
        players.forEach(player ->{
            player.sendPacket(packet);
        });
    }
    
    public Zone getZone(int zoneID) throws IllegalArgumentException {
        for(Zone z : zones)
            if(z.getID() == zoneID)
                return z;
        
        throw new IllegalArgumentException("No Zone with id = " + zoneID);
    }

    public Zone getZone(Vector3f location) {
        // TODO get the zone from the location
        return zones.get(0);
    }
	
}
