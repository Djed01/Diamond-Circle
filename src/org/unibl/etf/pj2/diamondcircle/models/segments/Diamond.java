package org.unibl.etf.pj2.diamondcircle.models.segments;

import javax.swing.*;
import java.awt.*;

public class Diamond implements Segment {
    private static final String DIAMOND_PATH = "src/resources/img/diamond-element.png";
    private final String imagePath;

    public Diamond() {
        this.imagePath = DIAMOND_PATH;
    }

    public ImageIcon getDiamondImage() {
        return new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(180, 250, Image.SCALE_DEFAULT));
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
