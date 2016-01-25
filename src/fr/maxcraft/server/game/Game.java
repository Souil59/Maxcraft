package fr.maxcraft.server.game;

import org.bukkit.World;

/**
 * Created by Crevebedaine on 23/01/2016.
 */
public class Game {

    private final String name;
    private final String source;
    private final String entrance;
    private final int max;
    private final int life;

    public Game(String name, String source, String entrance, int max,int life){
        this.name = name;
        this.source = source;
        this.entrance = entrance;
        this.max = max;
        this.life = life;

    }

    public String getName() {
        return name;
    }
    public String getSource() {
        return source;
    }
    public String getEntrance() {
        return entrance;
    }
    public int getMax() {
        return max;
    }

    public int getLife() {
        return life;
    }
}
