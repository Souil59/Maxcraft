package fr.maxcraft.server.quester;

import fr.maxcraft.player.User;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Crevebedaine on 10/02/2016.
 */
public class Note {

    private ArrayList<String> quest;
    private ArrayList<List<String>> note;
    private ArrayList<Location> target;
    private static HashMap<User,Note> notelist = new HashMap<User,Note>();

    public Note(User u){
        this.quest=new ArrayList<String>();
        this.note = new ArrayList<List<String>>();
        this.target = new ArrayList<Location>();
        notelist.put(u,this);
    }

    public static Note get(User u){
        if (notelist.containsKey(u))
            return notelist.get(u);
        return new Note(u);

    }

    public void add(String quest,ArrayList<String> note,Location target) {
        if (this.quest.contains(quest)){
            this.note.set(this.quest.indexOf(quest),note);
            this.target.set(this.quest.indexOf(quest),target);
            return;
        }
        this.quest.add(quest);
        this.note.add(note);
        this.target.add(target);
        return;
    }

    public ArrayList<List<String>> getNote() {
        return note;
    }

    public ArrayList<Location> getTarget() {
        return target;
    }

    public ArrayList<String> getQuest() {
        return quest;
    }
}
