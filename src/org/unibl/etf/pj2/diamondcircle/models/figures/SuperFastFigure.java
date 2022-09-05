package org.unibl.etf.pj2.diamondcircle.models.figures;

import org.unibl.etf.pj2.diamondcircle.models.segments.Color;

public class SuperFastFigure extends Figure{
    public SuperFastFigure(){
        super();
    }

    public SuperFastFigure(Color color){
        super(color);
    }

    public SuperFastFigure(Color color, int numOfSteps){
        super(color,numOfSteps);
    }

    @Override
    public void setNumOfSteps(int numOfSteps){
        super.setNumOfSteps(numOfSteps * 2);
    }

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName() + super.toString();
    }

    @Override
    public String getLabel(){
        return getClass().getSimpleName().substring(0,1);
    }

}
