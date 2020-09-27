package alx;

import javafx.util.Pair;
import model.Cards;
import model.PlayerAction;
import model.Wonder;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ALXController {
    HashMap<String, ALXPlayer> simPlayers;
    public ALXController(){
        simPlayers = new HashMap<String, ALXPlayer>();
    }

    public abstract void registerSimPlayer(ALXPlayer player);

    public abstract Pair<PlayerAction, Cards> getSimPlayAction(String wonderName, ArrayList<Cards> hand);

    public abstract Boolean getWonderSide(Wonder w);

    public abstract Cards getCopyGuild(ArrayList<Cards> guildChoices);

    public abstract Cards getPlayFromDiscard(ArrayList<Cards> playable);

}
