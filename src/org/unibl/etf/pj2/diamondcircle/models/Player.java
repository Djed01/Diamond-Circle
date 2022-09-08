package org.unibl.etf.pj2.diamondcircle.models;


import com.sun.source.tree.WhileLoopTree;
import org.unibl.etf.pj2.diamondcircle.Game;
import org.unibl.etf.pj2.diamondcircle.models.figures.BasicFigure;
import org.unibl.etf.pj2.diamondcircle.models.figures.Figure;
import org.unibl.etf.pj2.diamondcircle.models.figures.LevitatingFigure;
import org.unibl.etf.pj2.diamondcircle.models.figures.SuperFastFigure;
import org.unibl.etf.pj2.diamondcircle.models.segments.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Player extends Thread{
    public static final int NUMBER_OF_FIGURES = 4;
    private static int counter = 0;
    public final ReentrantLock LOCK = new ReentrantLock();

    private Figure currentFigure;
    private final String name;
    private int numOfFields = 0;
    private int currentFigureNumber = 0;

    private boolean isFinished = false;
    private boolean isStarted = false;

    private final ArrayList<Figure> figures = new ArrayList<>(NUMBER_OF_FIGURES);

    public Player(Color color){
        this("Igrac "+(++counter),color);
    }

    public Player(String name, Color color){
        this.name = name;
        generateFigures(color);
    }

    private void generateFigures(Color color){
        Random rand = new Random();
        for(int i=0;i<NUMBER_OF_FIGURES;i++){
            int type = rand.nextInt();
            Figure figure = null;
            switch (type){
                case 0:
                    figure = new BasicFigure(color);
                    break;
                case 1:
                    figure = new LevitatingFigure(color);
                    break;
                case 2:
                    figure = new SuperFastFigure(color);
                    break;
            }
            figures.add(figure);
        }
    }
    @Override
    public String toString(){
        return "Player{" + "name='" + name + '\'' +
                ", figures=" + figures +
                '}';
    }

    public String getPlayerName(){
        return name;
    }

    public  String getColor(){
        return figures.get(0).getColor().toString();
    }

    public ArrayList<String> getFigureNames(){
        return figures.stream().map(Figure::getFigureName).collect(Collectors.toCollection(ArrayList::new));
    }


    public String getResult(){
        StringBuilder sb = new StringBuilder(name + "\n");
        for(Figure f: figures){
            sb.append(f.getResult());
        }
        return sb.toString();
    }

    @Override
    public void run(){
        isStarted = true;
        synchronized (this.LOCK){
            while (currentFigureNumber != NUMBER_OF_FIGURES){
                try {
                    this.LOCK.wait();
                }catch (InterruptedException e){
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE,e.fillInStackTrace().toString());
                }

                currentFigure = figures.get(currentFigureNumber);
                currentFigure.setNumOfSteps(numOfFields);

                if(!currentFigure.isStarted()){
                    currentFigure.start();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
                    }
                }

                synchronized (currentFigure.LOCK){
                    currentFigure.LOCK.notify(); //started move
                    try {
                        currentFigure.LOCK.wait(); // waiting to end move
                        if(currentFigure.isFinished()){
                            currentFigureNumber++;
                        }
                    }catch (InterruptedException e){
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE,e.fillInStackTrace().toString());
                    }
                }
                LOCK.notify(); // ended move
            }
        }
        isFinished = true;
    }

    public void setNumOfFields(int numOfFields){
        this.numOfFields = numOfFields;
    }

    public boolean isFinished(){
        return isFinished;
    }

    public boolean isStarted(){
        return isStarted;
    }

    public Figure getCurrentFigure(){
        return currentFigure;
    }
}
