package org.unibl.etf.pj2.diamondcircle.models.segments;

public class Hole implements Segment {
    public String getColor() {
        return "BLACK";
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
