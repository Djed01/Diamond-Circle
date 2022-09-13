package org.unibl.etf.pj2.diamondcircle.models.cards;

import org.unibl.etf.pj2.diamondcircle.Game;
import org.unibl.etf.pj2.diamondcircle.models.figures.Figure;
import org.unibl.etf.pj2.diamondcircle.models.figures.Levitable;
import org.unibl.etf.pj2.diamondcircle.models.segments.Hole;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.unibl.etf.pj2.diamondcircle.Main.game;

public class SpecialCard extends Card {

    private static final String SPECIAL_CARD_IMAGE_PATH = "src/resources/img/special-card.png";
    private final int numOfHoles;
    private final ArrayList<Hole> holes = new ArrayList<>();

    public SpecialCard(int numOfHoles) {
        super(SPECIAL_CARD_IMAGE_PATH);
        this.numOfHoles = numOfHoles;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " Broj rupa: " + numOfHoles;
    }

    public void makeHoles() {
        ArrayList<Integer> tempPath = new ArrayList<>(); //Putanja za rupe
        ArrayList<Integer> nonLevitatingIndexes = new ArrayList<>();
        int i = 0;
        while (i < numOfHoles) {
            int randomIndex = new Random().nextInt(game.getPathSize());
            int pathSegment = game.getPathSegment(randomIndex);
            if (!tempPath.contains(pathSegment)) {
                tempPath.add(pathSegment);
                i++;
            }
        }
        int matrixDimension = game.getMatrixDimension();
        for (Integer index : tempPath) {
            Hole hole = new Hole();
            holes.add(hole);
            int currentIndex = index - 1;
            int x = currentIndex / matrixDimension;
            int y = currentIndex % matrixDimension;
            nonLevitatingIndexes.add(currentIndex);
            game.getAddHole().accept(hole, currentIndex); //Dodavanje rupa na GUI-u
            if ((game.matrix[x][y] instanceof Figure) && !(game.matrix[x][y] instanceof Levitable)) {
                ((Figure) game.matrix[x][y]).fallInsideHole(currentIndex); //Ako nije lebdeca, figura upada u rupu
                game.matrix[x][y] = null;
            }
        }
        //Nakon sto se saceka uklanjaju se sve rupe
        try {
            Thread.sleep(game.SLEEP_TIME);
        } catch (InterruptedException e) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
        }
        //Azuriranje GUI-a
        for (Integer index : nonLevitatingIndexes) {
            game.getRemoveHole().accept(index);
        }

    }
}
