package net.nathem.script.core.object;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Crevebedaine on 06/02/2016.
 */
public class CheckPoint extends NathemObject{

    private boolean global;
    public CheckPoint() {
        super();
    }

    public CheckPoint(Sign sign, NathemWorld nathemWorld) {
        super(sign, nathemWorld);
        this.global = (boolean) this.getOption("global");
    }
    @Override
    public void activate() {

    }

    @Override
    public HashMap<String, Option> getOptionsList() {

        HashMap<String, Option> optionsList = new HashMap<String, Option>();
        optionsList.put("text", new Option("text", "Text message here !", OptionType.TEXT));
        optionsList.put("global", new Option("global", "false", OptionType.BOOLEAN));
        return optionsList;
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.CHECKPOINT;
    }

    @Override
    public void onSignal(boolean value, Player p) {
        if(value&&!global)
            this.nathemWorld.getCheckPoints().put(p, this.getLocation().clone().add(0.5,0,0.5));
        if (value&&global)
            for (Player pl : this.location.getWorld().getPlayers())
                this.nathemWorld.getCheckPoints().put(pl, this.getLocation().clone().add(0.5,0,0.5));

    }
}
