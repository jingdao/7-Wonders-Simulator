package alx;

import javafx.util.Pair;
import model.*;
import view.CardView;

import java.util.ArrayList;
import java.util.HashSet;

public class ALXPlayer extends Player {
    private ALXController alxControl;

    public ALXPlayer(int id, Wonder w, CardView v, ALXController alxControl) {
        super(id, w, v, false);
        this.alxControl = alxControl;
        alxControl.registerSimPlayer(this);
    }

    public void getAction(ArrayList<Cards> cards) {
        Pair<PlayerAction, Cards> play = alxControl.getSimPlayAction(wonder.name, cards);
        action = play.getKey();
        lastCard = play.getValue();
    }

    public void selectWonderSide() {
        isWonderBSide= alxControl.getWonderSide(wonder);
    }

    public void copyGuild() {
        ArrayList<Cards> guildChoices = new ArrayList<Cards>();
        for (Cards c:left.playedCards) if (c.type== CardType.PURPLE) guildChoices.add(c);
        for (Cards c:right.playedCards) if (c.type==CardType.PURPLE) guildChoices.add(c);
        Cards addedGuild = alxControl.getCopyGuild(guildChoices);
        if (!(addedGuild == null)) playedCards.add(addedGuild);
    }

    public void playFromDiscard(ArrayList<Cards> discardPile) {
        canPlayFromDiscard=false;
        ArrayList<Cards> selection = new ArrayList<Cards>();
        HashSet<Cards> selected = new HashSet<Cards>();
        for (Cards c:discardPile) {
            if (playedCards.contains(c)) selection.add(null);
            else if (selected.contains(c)) selection.add(null);
            else {selection.add(c); selected.add(c);}
        }

        Cards play = alxControl.getPlayFromDiscard(selection);
        if(!(play == null)){
            playedCards.add(play);
            applyCardEffect(play);
            discardPile.remove(play);
        }
    }
}
