package server.game.spells;

import braynstorm.commonlib.math.Vector3f;

public class ShapeCircle extends Shape {
    
    private float radiusSquared;
    
    public ShapeCircle(Vector3f position, float radius) {
        super(position);
        this.radiusSquared = radius * radius;
    }

    @Override
    public boolean isPointInShape(Vector3f point) {
        return Vector3f.getDistanceSquared(position, point) <= radiusSquared;
    }

}
