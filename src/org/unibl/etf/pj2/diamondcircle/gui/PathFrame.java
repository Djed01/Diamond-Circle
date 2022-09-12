package org.unibl.etf.pj2.diamondcircle.gui;

import org.unibl.etf.pj2.diamondcircle.Main;
import org.unibl.etf.pj2.diamondcircle.models.figures.Figure;
import org.unibl.etf.pj2.diamondcircle.models.figures.FigureMovement;

import javax.print.attribute.standard.JobKOctets;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static org.unibl.etf.pj2.diamondcircle.Main.game;

public class PathFrame extends JFrame {
    private int matrixDimension;
    private JPanel centralPanel;
    private JPanel topPanel;
    private JLabel matrixLabel[][];

    private JLabel timeLabel;
    private static final String IMG_PATH_PREFIX = "src/resources/img/";
    public PathFrame() {
        // Podesavanje Frame-a
        ImageIcon appIcon = new ImageIcon(IMG_PATH_PREFIX+"logo.png");
        this.setIconImage(appIcon.getImage());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setBackground(new Color(236, 239, 244));
        setBounds(500, 350, 1000, 650);
        this.setResizable(false);
        this.setVisible(true);
        this.setLayout(null);
        this.setTitle("FigurePath");


        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBounds(400,10, 200,50);
        this.getContentPane().add(topPanel);

        timeLabel = new JLabel();
        timeLabel.setVerticalAlignment(JLabel.CENTER);
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setVisible(true);
        topPanel.add(timeLabel);

        //Matrica
        matrixDimension = Main.game.getMatrixDimension();
        centralPanel = new JPanel(new GridLayout(matrixDimension, matrixDimension));
        centralPanel.setBackground(Color.white);
        centralPanel.setBounds(140, 70, 720, 500);
        centralPanel.setVisible(true);

        matrixLabel = new JLabel[matrixDimension][matrixDimension];
        int k = 0;
        for (int i = 0; i < matrixDimension; i++)
            for (int j = 0; j < matrixDimension; j++) {
                matrixLabel[i][j] = new JLabel(String.valueOf(++k));
                matrixLabel[i][j].setHorizontalAlignment(JLabel.CENTER);
                matrixLabel[i][j].setVerticalAlignment(JLabel.CENTER);
                matrixLabel[i][j].setVisible(true);
                centralPanel.add(matrixLabel[i][j]);
            }

        FigureMovement figureMovement = FigureMovement.deserialize(DiamonCircleFrame.getFilePath());
        showFigurePath(figureMovement);
        this.getContentPane().add(centralPanel);

    }

    private void showFigurePath(FigureMovement figureMovement){
        Color color;
        try {
            Field field = Class.forName("java.awt.Color").getField(figureMovement.getColor().toString().toLowerCase());
            color = (Color) field.get(null);
        } catch (Exception e) {
            color = null; // Not defined
        }
        String label = figureMovement.getLabel();
        ArrayList<Integer> crossedFields = figureMovement.getCrossedFields();
        String movementTime = figureMovement.getMovementTime();
        timeLabel.setText("<html><div style='text-align: center;'>Vrijeme kretanja:<br>"+movementTime+"</div></html>");
        int dimension = matrixLabel.length;
        for(Integer fieldIndex:crossedFields){
            fieldIndex--;
            matrixLabel[fieldIndex/dimension][fieldIndex%dimension].setBackground(color);
            matrixLabel[fieldIndex/dimension][fieldIndex%dimension].setText(label);
            matrixLabel[fieldIndex/dimension][fieldIndex%dimension].setOpaque(true);
        }


    }
}
