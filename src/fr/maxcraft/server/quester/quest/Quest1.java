package fr.maxcraft.server.quester.quest;

import fr.maxcraft.player.User;
import fr.maxcraft.server.quester.Quest;
import fr.maxcraft.server.quester.Quester;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;


/**
 * Created by Crevebedaine on 08/02/2016.
 */
public class Quest1 extends Quest{
    private final Quester quester;

    public Quest1(Quester q) {
        super(q);
        this.quester = q;
    }

    @Override
    public int rightclic(User u, int i) {
        if (i==0) {
            chat(u, "Mais qu'en ais-je fait ? Où est cette foutu clèf ? Tiens ! Qui êtes vous ? Oh et puisque vous êtes là, souhaiteriez vous m'aider ? J'ai perdu la clef permettant l'ouverture de ce coffre !");
            ask(u, "Bien sur.", 1, ChatColor.BLUE);
            ask(u, "J'ai autres chose à faire.", 2, ChatColor.RED);
            return 0;
        }
        return i;
    }

    @Override
    public int leftclic(User u, int i) {
        return i;
    }

    @Override
    public int answer(User u, int i, int answer) {
        return i;
    }
}
