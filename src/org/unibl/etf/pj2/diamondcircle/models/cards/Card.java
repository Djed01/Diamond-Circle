package org.unibl.etf.pj2.diamondcircle.models.cards;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public abstract class Card {
    private final String imagePath;

    public Card(String imagePath) {
        this.imagePath = imagePath;
    }

    public ImageIcon getCardImage() {
        return new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(180, 250, Image.SCALE_DEFAULT));
    }
}
