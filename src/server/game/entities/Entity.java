package server.game.entities;

import braynstorm.commonlib.Common;
import braynstorm.commonlib.math.Vector3f;

public class Entity {
    protected int displayID;
    
    protected Vector3f position;
    protected Vector3f forward;
    protected Vector3f up;
    
    protected boolean isVisible;
    protected Dirtyness dirtyFlags;
    
    public Entity(int displayID, Vector3f position, boolean isVisible) {
        this.displayID = displayID;
        this.position = position;
        this.forward = new Vector3f(0, 0, 1);
        this.up = new Vector3f(0, 1, 0);
        this.isVisible = isVisible;
        
        dirtyFlags = new Dirtyness();
    }
    
    public void lookAt(Vector3f point){
        forward = point.getNormalized();
        up = forward.cross(Vector3f.Y_AXIS).cross(forward);
        markDirty(Dirtyness.MOTION);
    }
    
    public void setPosition(Vector3f position) {
        this.position = position;
        markDirty(Dirtyness.MOTION);
    }
    
    public void setForward(Vector3f forward) {
		this.forward = forward;
		markDirty(Dirtyness.MOTION);
	}

	public void setUp(Vector3f up) {
		this.up = up;
		markDirty(Dirtyness.MOTION);
	}
	
	public void move(Vector3f direction, float amount){
		this.position.add(direction.getMul(amount));
		markDirty(Dirtyness.MOTION);
	}
	
	/**
     * FIXME Possibly HEAVY
     * @param angle
     */
    public void rotateX(float angle){
        if(angle == 0f)
            return;
        
        Vector3f horizontalAxis = Common.Y_AXIS.cross(forward).normalize();
        
        forward.rotate(angle, horizontalAxis).normalize();
        up = forward.cross(horizontalAxis).normalize();
        
        markDirty(Dirtyness.MOTION);
    }
    
    /**
     * FIXME Possibly HEAVY
     * @param angle
     */
    public void rotateY(float angle){
        if(angle == 0f)
            return;
        
        Vector3f horizontalAxis = Common.Y_AXIS.cross(forward).normalize();
        
        forward.rotate(angle, Common.Y_AXIS).normalize();
        up = forward.cross(horizontalAxis).normalize();
        
        markDirty(Dirtyness.MOTION);
    }
    
    protected void markDirty(byte type) {
		dirtyFlags.markDirty(type);
	}

    public void markClean(byte type) {
		dirtyFlags.markClean(type);
	}

	public boolean isDirty(byte type) {
		return dirtyFlags.getDirty(type);
	}

	public int getDisplayID(){ return displayID; }
    public boolean isVisible(){ return isVisible; }
    public Vector3f getPosition(){ return position; }
    public Vector3f getForward(){ return forward; }
    public Vector3f getUp(){ return up; }
    
}
