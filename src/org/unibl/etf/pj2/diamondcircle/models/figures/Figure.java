package org.unibl.etf.pj2.diamondcircle.models.figures;

import org.unibl.etf.pj2.diamondcircle.Game;
import org.unibl.etf.pj2.diamondcircle.Main;
import org.unibl.etf.pj2.diamondcircle.models.segments.Color;
import org.unibl.etf.pj2.diamondcircle.models.segments.Diamond;
import org.unibl.etf.pj2.diamondcircle.models.segments.Segment;

import javax.swing.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.time.Duration.ofMillis;
import static org.unibl.etf.pj2.diamondcircle.Main.game;

public abstract class Figure extends Thread implements Segment {

    private static int num = 0;
    private String name;
    private Color color;
    private int numOfDiamonds = 0;
    private int numOfSteps = 0;
    private int numOfCrossedFields = 0;
    private long time = 0;
    private boolean isFinished = false;
    private boolean isStarted = false;
    private boolean isFallen = false;
    private int startPosition = 0;
    private int endPosition = 0;

    private final ArrayList<Integer> crossedFields = new ArrayList<>();
    public final ReentrantLock LOCK = new ReentrantLock();

    public Figure() {

    }

    public Figure(Color color) {
        this.color = color;
        name = "Figura" + (++num);
    }

    public Figure(Color color, int numOfSteps) {
        this.color = color;
        this.numOfSteps = numOfSteps;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setNumOfSteps(int numOfSteps) {
        this.numOfSteps = numOfSteps;
    }

    @Override
    public void run() {
        isStarted = true;
        synchronized (LOCK) {
            while (!game.isLastField(numOfCrossedFields)) {
                try {
                    LOCK.wait(); //Cekanje na potez
                } catch (InterruptedException e) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
                }

                long start = System.currentTimeMillis();
                if (isFallen) {
                    isFinished = true;
                } else {
                    int matrixDim = game.getMatrixDimension();
                    startPosition = game.getPathSegment(numOfCrossedFields);

                    //Nastavljanje kretanja od pozicije na kojoj je figura stala u prethodnom potezu
                    int currentIndex, indexBeforeMove;
                    if (numOfCrossedFields > 0 && numOfSteps + numOfDiamonds > 0) {
                        indexBeforeMove = game.getPathSegment(numOfCrossedFields - 1) - 1;
                        int x = indexBeforeMove / matrixDim;
                        int y = indexBeforeMove % matrixDim;
                        if (game.matrix[x][y] instanceof Diamond) {
                            pickDiamond(indexBeforeMove, x, y);
                        }
                        game.getRemoveFigure().accept(indexBeforeMove);
                        game.matrix[x][y] = null;
                    }
                    int x, y, temp = numOfCrossedFields;
                    int numberOfStepsTemp = numOfSteps + 1;
                    for (int i = 0; numOfCrossedFields < game.getPathSize() && i < numberOfStepsTemp + numOfDiamonds; i++, numOfCrossedFields++) {
                        //Ukoliko se igra pauzira, pauziramo i kretanje figure
                        synchronized (game.PAUSE_LOCK) {
                            try {
                                if (game.isPause())
                                    game.PAUSE_LOCK.wait(); //Cekamo dok se igra ne pokrene
                            } catch (InterruptedException e) {
                                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
                            }
                        }
                        currentIndex = game.getPathSegment(numOfCrossedFields) - 1;
                        x = currentIndex / matrixDim;
                        y = currentIndex % matrixDim;
                        if (temp + numberOfStepsTemp + numOfDiamonds - 1 < game.getPathSize() - 1) {
                            endPosition = game.getPathSegment(temp + numberOfStepsTemp + numOfDiamonds - 1);
                        }

                        Segment segment = game.matrix[x][y];
                        if (segment instanceof Diamond)
                            pickDiamond(currentIndex, x, y);

                        while (segment instanceof Figure && (i + 1 == numberOfStepsTemp + numOfDiamonds))
                            numberOfStepsTemp++;

                        if (!(segment instanceof Figure)) {
                            game.matrix[x][y] = this; //azuriranje matrice
                            game.getAddFigure().accept(this, currentIndex); //azuriranje GUI-a dodavanjem figure
                        }

                        try {
                            Thread.sleep(game.SLEEP_TIME); //Kretanje svake sekunde
                        } catch (InterruptedException e) {
                            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
                        }

                        //Ujlanjanje figure akko nije kraj kretanja ili  je dosla do cilja
                        if ((i + 1 < numberOfStepsTemp + numOfDiamonds || game.isLastField(numOfCrossedFields + 1)) && !(segment instanceof Figure)) {
                            game.getRemoveFigure().accept(currentIndex); //Uklanjanje figure sa GUI-a
                            game.matrix[x][y] = null; //Uklanjanje iz matrice
                        }
                        crossedFields.add(currentIndex + 1);
                    }

                }
                long end = System.currentTimeMillis();
                time += (end - start);
                Duration duration = Duration.ofMillis(time);
                // Cuvanje putanje kretanja
                new FigureMovement(this.name, this.getLabel(), String.format("%02d:%02d:%02d", (int) duration.toHours(), (int) duration.toMinutes(), (int) duration.toSeconds()), this.color, this.crossedFields);
                LOCK.notify(); // Obavjestavanje o kraju kretanja
            }
        }
        isFinished = true;
    }

    private void pickDiamond(int index, int x, int y) {
        game.getRemoveDiamond().accept(index); //Uklanjanje dijamanta sa GUI-a
        numOfDiamonds++;
        game.matrix[x][y] = null; // Uklanjanje sa matrice
    }

    public String getResult() {
        return String.format("\t%s (%s, %s) - pređeni put %s - stigla do cilja: %s\n", name, getType(), color, crossedFields,
                (Main.game.isLastField(numOfCrossedFields) ? "Da" : "Ne"));
    }

    public String getType() {
        return getClass().getSimpleName();
    }

    public String getLabel() {
        return getClass().getSimpleName().substring(0, 1);
    }

    public String getFigureName() {
        return name;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public int getNumOfFields() {
        return numOfSteps + numOfDiamonds;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void fallInsideHole(int index) {
        game.getRemoveFigure().accept(index); //Uklanjanje figure sa GUI-a
        isFallen = true;
    }


}
