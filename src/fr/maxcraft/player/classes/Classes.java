package fr.maxcraft.player.classes;

import fr.maxcraft.player.User;

import java.util.UUID;

/**
 * Created by Crevebedaine on 13/02/2016.
 */
public abstract class Classes{

    private ClassesType type;
    private User user;
    private double xp;
    private double mana;





    public double getMana() {
        return mana;
    }
    public void setMana(double mana) {
        this.mana = mana;
    }
    public int getLevel() {
        return (int) Math.sqrt(this.xp);
    }
    public User getUser() {
        return this.user;
    }
    public int getMaxMana(){
        if (this.type.equals(ClassesType.MAGE))
            return 100+this.getLevel()*10;
        return 100;
    }
    public void manaConsume(int how){
        this.mana -= how;
    }

}
