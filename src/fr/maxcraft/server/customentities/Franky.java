package fr.maxcraft.server.customentities;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

/**
 * Created by Crevebedaine on 26/01/2016.
 */
public class Franky extends EntityWolf {

    public Franky(net.minecraft.server.v1_8_R3.World w) {
        super(w);
        NBTTagCompound tag = new NBTTagCompound();
        this.c(tag);
        tag.setBoolean("Invulnerable", true);
        this.f(tag);
        this.setCustomNameVisible(true);
        this.setCustomName(ChatColor.GREEN+"Franky");
        this.goalSelector = new PathfinderGoalSelector(w != null && w.methodProfiler != null?w.methodProfiler:null);
        this.goalSelector.a(2, new PathfinderGoalFollowOwner(this, 1.5D, 5.0F, 2.0F));
        this.goalSelector.a(3, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(5, new net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.goalSelector.a(1, new PathfinderGoalTempt(this, 1.2D, Items.CARROT, false));
    }

    public static Franky spawn(Player p){
        Franky e = new Franky(((CraftWorld) p.getWorld()).getHandle());
        e.setOwnerUUID(p.getUniqueId()+"");
        EntityTypes.spawnEntity(e,p.getLocation());
        return e;

    }
    @Override
    public EntityAgeable createChild(EntityAgeable entityAgeable) {
        return null;
    }
    protected String bo() {
        return "mob.pig.death";
    }

    protected String bp() {
        return "mob.pig.death";
    }

    protected String z() {
        return "mob.pig.say";
    }

}
