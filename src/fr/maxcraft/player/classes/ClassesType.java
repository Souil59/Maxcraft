package fr.maxcraft.player.classes;

import fr.maxcraft.player.classes.classes.*;

/**
 * Created by Crevebedaine on 13/02/2016.
 */
public enum ClassesType {

    BERSERKER(Berserker.class),
    ARCHER(Archer.class),
    INFANTRY(Infantry.class),
    CAVALRY(Cavalry.class),
    MAGE(Mage.class);

    ClassesType(Class<? extends Classes> cla){

    }
}
