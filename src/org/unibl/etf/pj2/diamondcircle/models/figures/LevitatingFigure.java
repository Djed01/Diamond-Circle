package org.unibl.etf.pj2.diamondcircle.models.figures;

import org.unibl.etf.pj2.diamondcircle.models.segments.Color;

public class LevitatingFigure extends Figure implements Levitable {

    public LevitatingFigure() {
        super();
    }

    public LevitatingFigure(Color color) {
        super(color);
    }

    public LevitatingFigure(Color color, int numOfSteps) {
        super(color, numOfSteps);
    }

    @Override
    public String getLabel() {
        return getClass().getSimpleName().substring(0, 1);
    }

    public String getType() {
        return getClass().getSimpleName();
    }
}
