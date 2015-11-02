package server.game.powers;

import server.game.entities.EntityLiving;

public abstract class Power {
    protected String name;
    protected float max;
    protected float current;
    protected float regenRate = 0;
    
    public abstract void tick(EntityLiving entity);

    public Power(String name, float max, float current) {
        this.name = name;
        setMax(max);
        setCurrent(current);
    }
    
    public void setMax(float max){
        this.max = max;
    }
    
    public boolean hasEnough(float amount){
        if(current >= amount)
            return true;
        return false;
    }
    
    /**
     * Depletes the resource
     * @param amount Amount to use 
     * @param doUse if its false, it won't use the mana but will return the amount that can be used
     * @return Amount used up
     */
    public float use(float amount, boolean doUse){
        if(doUse){
            float usedUp = 0f;
            
            if(getCurrent() >= amount){
                usedUp = amount;
                setCurrent(getCurrent() - usedUp);
            }
            else{
                usedUp = getCurrent();
                setCurrent(0);
            }
            
            return usedUp;
        }else{
            if(getCurrent() >= amount)
                return amount;
            return getCurrent();
        }
    }
    
    /**
     * Adds to the current power level.
     * @param amount Amount to add
     * @return the amount leftover
     */
    public float add(float amount) {
        if(amount < 0)
            throw new IllegalArgumentException("Adding negative power. Use 'use(amount, true)' instead.");
        
        if(amount == 0)
            return 0;
        
        if(getMax() == getCurrent())
            return amount;
        
        float newCurrent = getCurrent() + amount;
        
        if(newCurrent <= getMax()){
            setCurrent(newCurrent);
            return 0;
        }
        
        setCurrent(getMax());
        return getMax() - newCurrent;
    }
    
    public boolean isZero(){
        return current <= 0.001f;
    }
    
    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public float getMax() {
        return max;
    }

    public String getName() {
        return name;
    }

    public float getRegenRate() {
        return regenRate;
    }

    public void setRegenRate(float regenRate) {
        this.regenRate = regenRate;
    }
    
    
}
