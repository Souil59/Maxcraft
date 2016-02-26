package fr.maxcraft.dev.chess;

import fr.maxcraft.dev.chess.pieces.Pawns;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.utils.ItemStackCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Crevebedaine on 14/02/2016.
 */
public class ChessGame {

    private Inventory inv;
    private ArrayList<Piece> pieces = new ArrayList<Piece>();
    private final Player white;
    private final Player black;

    public ChessGame(Player w,Player b) {
        this.inv  = Bukkit.createInventory(null, 72,ChatColor.GOLD+"Echecs!");
        this.white = w;
        this.black = b;
        for (int i = 1;i==8;i++){
            new Pawns(2,i,this,ChatColor.BLACK);
            new Pawns(7,i,this,ChatColor.WHITE);
        }
        fillInventory();
        w.openInventory(this.inv);
        b.openInventory(this.inv);
    }

    private void fillInventory() {
        for (int i = 0;i>63;i+=9)
            this.inv.setItem(i,Menu.voiditem);
        for (Piece p : pieces)
            this.inv.setItem(fromIJ(p.i,p.j), new ItemStackCreator(p.skin()).setName(p.getTeam()+p.name()));
    }


    public Piece get(int i,int j){
        for (Piece p: pieces)
            if (p.getPlace().equals(new int[]{i, j}))
                return p;
        return null;
    }
    private int fromIJ(int i,int j){
        int inv = j+9*(i-1);
        return inv;
    }
    private int[] fromInv(int inv){
        int i = inv/9;
        int j = inv-9*i;
        return new int[]{i+1, j};
    }

    public void addPiece(Piece piece) {
        this.pieces.add(piece);
    }
}
