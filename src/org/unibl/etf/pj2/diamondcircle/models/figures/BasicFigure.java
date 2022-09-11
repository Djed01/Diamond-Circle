package org.unibl.etf.pj2.diamondcircle.models.figures;

import org.unibl.etf.pj2.diamondcircle.models.segments.Color;

public class BasicFigure extends Figure {

    public BasicFigure(Color color) {
        super(color);
    }

    public BasicFigure(Color color, int numOfSteps) {
        super(color, numOfSteps);
    }

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName() + super.toString();
    }

    @Override
    public String getLabel() {
        return getClass().getSimpleName().substring(0, 1);
    }

    public String getType() {
        return getClass().getSimpleName();
    }
}
