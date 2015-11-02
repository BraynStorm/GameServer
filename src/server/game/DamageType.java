package server.game;

public enum DamageType {
    PHYSICAL        (1),
    FIRE            (2),
    FROST           (2),
    LIGHT           (2),
    SHADOW          (2),
    NATURE          (2),
    ARCANE          (2);
    
    private int type;
    
    private DamageType(int type) {
        this.type = type;
    }
    
    public boolean isPhysical(){
        return type == 1;
    }
    
    public boolean isMagic(){
        return type == 2;
    }
}
