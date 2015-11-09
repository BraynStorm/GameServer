package server.game.spells;

import braynstorm.commonlib.math.Vector3f;

public abstract class Shape {
    
    protected Vector3f position;
    
    public Shape(Vector3f position){
        this.position = position;
    }
    
    public abstract boolean isPointInShape(Vector3f point);
}
