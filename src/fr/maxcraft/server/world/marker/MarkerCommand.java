package fr.maxcraft.server.world.marker;

import fr.maxcraft.server.command.Command;
import fr.maxcraft.server.world.Travel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Crevebedaine on 07/02/2016.
 */
public class MarkerCommand extends Command{
    public MarkerCommand(String warp) {
        super(warp);
        this.setPerms("maxcraft.guide").setAliases(Arrays.asList("marker","lieu")).register();
        this.tabComplete(warp,Arrays.asList("tp","create","delete","travel"));
        this.tabComplete("travel",Arrays.asList("create","delete"));
    }

    @Override
    public boolean execute(CommandSender arg0, String arg1, String[] args) {
        if (args.length==0){
            ArrayList<String> name = new ArrayList<String>();
            for (Marker m : Marker.getMarkerlist())
                name.add(m.getName());
            arg0.sendMessage(""+name);
            return true;
        }
        if (args[0].equals("tp"))
            tp((Player) arg0,args);
        if (!arg0.hasPermission("maxcraft.modo"))
            return true;
        if (args[0].equals("new")||args[0].equals("create"))
            create((Player) arg0,args);
        if (args[0].equals("remove")||args[0].equals("delete"))
            remove((Player) arg0,args);
        if (args[0].equals("travel"))
            travel((Player) arg0,args);
            return true;
    }

    private void travel(Player arg0, String[] args) {
        if (args.length==5){
            if (args[2].equals("create")&&(Marker.getMarker(args[4])==null||Marker.getMarker(args[3])==null||Marker.getMarker(args[2])==null)) {
                arg0.sendMessage("Un ou plusieurs des markers n'existe pas!");
                return;
            }
            new Travel(Marker.getMarker(args[2]),Marker.getMarker(args[3]),Marker.getMarker(args[4])).insert();
            arg0.sendMessage("Travel ajouté");
            return;
        }
        if (args.length==3){
            if (args[2].equals("delete")&&Marker.getMarker(args[2])==null) {
                arg0.sendMessage("Le marker n'existe pas!");
                return;
            }
            int i = 0;
            for (Travel t : (ArrayList<Travel>) Travel.travelslist.clone())
                if (t.contains(Marker.getMarker(args[2]))) {
                    t.remove();
                    i++;
                }
            arg0.sendMessage(i+" travels passant par "+args[2]+" supprimé(s)");
            return;
        }
        arg0.sendMessage("/warp travel create [marker1] [marker2] [marker3]");
        arg0.sendMessage("/warp travel delete [marker1]");
    }

    private void tp(Player p,String[] args){
        if (args.length == 1) {
            p.sendMessage("/marker tp [nom]");
            return;
        }
        if (Marker.getMarker(args[1]) == null) {
            p.sendMessage("Ce marker n'existe pas");
            return;
        }
        p.teleport(Marker.getMarker(args[1]));

    }

    private void create(Player p,String[] args){
        if (args.length == 1) {
            p.sendMessage("/marker new [nom] <onGPS?>");
            return;
        }
        if (args.length == 2)
            new Marker(p.getLocation(), args[1], false).insert();
        if (args.length == 3)
            new Marker(p.getLocation(), args[1], Boolean.getBoolean(args[2])).insert();
        p.sendMessage("Marker créé");

    }

    private void remove(Player p,String[] args){
        if (args.length == 1) {
            p.sendMessage("/marker remove [nom]");
            return;
        }
        if (Marker.getMarker(args[1]) == null) {
            p.sendMessage("Ce marker n'existe pas");
            return;
        }
        Marker.getMarker(args[1]).remove();
        p.sendMessage("Marker supprimé");

    }
}
