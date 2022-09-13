package org.unibl.etf.pj2.diamondcircle.models.figures;

import org.unibl.etf.pj2.diamondcircle.Game;
import org.unibl.etf.pj2.diamondcircle.models.segments.Diamond;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.unibl.etf.pj2.diamondcircle.Main.game;

// Singleton klasa
public class GhostFigure extends Thread {

    private final ArrayList<Integer> path = new ArrayList<>();
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static GhostFigure ghostFigure = null;

    //Privatan konstruktor
    private GhostFigure() {
        generatePath();
    }

    //F-ja koja vraca sam objekat GhostFigure
    public static GhostFigure getGhostFigure() {
        if (ghostFigure == null)
            ghostFigure = new GhostFigure();
        return ghostFigure;
    }

    private void generatePath() {
        //Generisanje putanje duh figure
        path.clear();
        ArrayList<Integer> tempPath = new ArrayList<>();
        int numOfDiamonds = random.nextInt(2, game.getMatrixDimension() + 1);
        int i = 0;
        while (i < numOfDiamonds) {
            int randomIndex = random.nextInt(game.getPathSize());
            int pathSegment = game.getPathSegment(randomIndex);
            if (!tempPath.contains(pathSegment)) {
                tempPath.add(pathSegment);
                i++;
            }
        }
        for (int j = 0; j < game.getPathSize(); j++) {
            int pathSegment = game.getPathSegment(j);
            if (tempPath.contains(pathSegment))
                path.add(pathSegment);
        }
    }

    @Override
    public void run() {
        int matrixDimension = game.getMatrixDimension();
        while (!game.isGameOver()) {
            //Dodavanje dijamanata
            for (Integer diamondIndex : path) {
                if (game.isGameOver()) break;
                synchronized (game.PAUSE_LOCK) {
                    if (game.isPause()) {
                        try {
                            game.PAUSE_LOCK.wait(); //Ako se igra pauzira cekamo i sa postavljanjem dijamanata(pauziramo nit duh figure)
                        } catch (InterruptedException e) {
                            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
                        }
                    }
                }
                diamondIndex--;
                int x = diamondIndex / matrixDimension;
                int y = diamondIndex % matrixDimension;
                if (game.matrix[x][y] == null) {
                    Diamond diamond = new Diamond();
                    game.matrix[x][y] = diamond; //Dodavanje dijamanta u matrici
                    game.getAddDiamond().accept(diamond, diamondIndex);//Azuriranje GUI-a
                }
                //Svakih 5 sekundi duh figura postavlja nove dijamante
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
                }
            }
            generatePath(); //Generisanje nove putanje duh figure
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
