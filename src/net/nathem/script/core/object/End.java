package net.nathem.script.core.object;

import fr.maxcraft.server.game.GameInstance;
import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Louis on 23/01/2016.
 */
public class End extends NathemObject {

    public End() {
        super();
    }

    public End(Sign sign, NathemWorld nathemWorld) {
        super(sign, nathemWorld);

    }
    @Override
    public void activate() {

    }

    @Override
    public HashMap<String, Option> getOptionsList() {
        return new HashMap<String, Option>();
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.END;
    }

    @Override
    public void onSignal(boolean value, Player p) {
        if (value);
            for (GameInstance g : GameInstance.getInstances())
            if (g.getInstanceWorld()!=null)
                if (g.getInstanceWorld().equals(p.getWorld()))
                    g.destroy();

    }
}
