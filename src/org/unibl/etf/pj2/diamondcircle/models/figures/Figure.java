package org.unibl.etf.pj2.diamondcircle.models.figures;

import org.unibl.etf.pj2.diamondcircle.models.segments.Color;
import org.unibl.etf.pj2.diamondcircle.models.segments.Segment;

import java.util.ArrayList;

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

    public Figure(){

    }

    public Figure(Color color){
        this.color = color;
        name = "Figura" +(++num);
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
}
