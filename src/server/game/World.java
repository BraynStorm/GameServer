package server.game;

import java.util.ArrayList;
import java.util.List;

import server.game.entities.EntityLiving;

public class World {

    private List<Zone> zones;
    private List<GameCharacter> characters;
    
    public List<EntityLiving> getEntitiesInShape(Shape shape){
        // TODO make shapes like circle , sphere, square, box, 'path'
        
        List<EntityLiving> resultList = new ArrayList<EntityLiving>();
        characters.forEach(c -> {
            if(shape.isPointInShape(c.getPosition()))
                resultList.add(c);
        });
    }
    
    public Zone getZone(int zoneID) throws IllegalArgumentException {
        for(Zone z : zones)
            if(z.getID() == zoneID)
                return z;
        
        throw new IllegalArgumentException("No Zone with id = " + zoneID);
    }
	
}
