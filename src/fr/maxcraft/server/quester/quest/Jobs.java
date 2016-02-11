package fr.maxcraft.server.quester.quest;

import fr.maxcraft.player.User;
import fr.maxcraft.player.jobs.jobs.Bucheron;
import fr.maxcraft.player.jobs.jobs.Excavateur;
import fr.maxcraft.player.jobs.jobs.Fermier;
import fr.maxcraft.player.jobs.jobs.Mineur;
import fr.maxcraft.server.economy.Account;
import fr.maxcraft.server.quester.Quest;
import fr.maxcraft.server.quester.Quester;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by Crevebedaine on 08/02/2016.
 */
public class Jobs extends Quest{
    public Jobs(Quester q) {
        super(q);
    }

    @Override
    protected int rightclic(User u, int i) {
        chat(u,"Tu souhaite travailler? bien sur! je te laisse choisir un metier!");
        ask(u,"Bucheron",1, ChatColor.GREEN);
        ask(u,"Mineur",2, ChatColor.RED);
        ask(u,"Fermier",3, ChatColor.GOLD);
        ask(u,"Excavateur",4, ChatColor.BLUE);
        return 0;
    }

    @Override
    protected int leftclic(User u, int i) {
        return 0;
    }

    @Override
    protected int answer(User u, int i, int answer) {
        if (answer==1)
            u.setJob(new Bucheron(u.getUuid(),0));
        if (answer==2)
            u.setJob(new Mineur(u.getUuid(),0));
        if (answer==3)
            u.setJob(new Fermier(u.getUuid(),0));
        if (answer==4)
            u.setJob(new Excavateur(u.getUuid(),0));
        chat(u,"Bravo, tu n'es plus un simple connard, tu es quelqu'un d'actif, riche et respectable desormais!");
        chat(u,"Enfin, c'est a peux pres le même...");
        u.getJob().insert();
        return 0;
    }
}
