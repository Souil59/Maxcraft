package fr.maxcraft.server.customentities.custompathfinder;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;

/**
 * Created by Crevebedaine on 26/01/2016.
 */
public class PathfinderGoalWalkToLoc extends PathfinderGoal
{
    private double speed;

    private EntityInsentient entity;

    private Location loc;

    private NavigationAbstract navigation;

    public PathfinderGoalWalkToLoc(EntityInsentient entity, Location loc, double speed)
    {
        this.entity = entity;
        this.loc = loc;
        this.navigation = this.entity.getNavigation();
        this.speed = speed;
    }

    public boolean a()
    {
        return true;
    }
    public void c()
    {
        PathEntity pathEntity = this.navigation.a(loc.getX(), loc.getY(), loc.getZ());

        this.navigation.a(pathEntity, speed);
    }
}

