package org.unibl.etf.pj2.diamondcircle.gui;

import org.unibl.etf.pj2.diamondcircle.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultsFrame extends JFrame {
    JList list;
    JPanel listPanel;
    JPanel textPanel;
    JTextArea textArea;
    private static final String IMG_PATH_PREFIX = "src/resources/img/";
    public static final String RESULTS_PATH = "src/resources/results/";

    ResultsFrame() {
        // Podesavanje Frame-a
        ImageIcon appIcon = new ImageIcon(IMG_PATH_PREFIX + "logo.png");
        this.setIconImage(appIcon.getImage());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setBackground(new Color(236, 239, 244));
        this.setBounds(375, 350, 1250, 650);
        this.setLayout(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setLayout(null);
        this.setTitle("Results");

        // List panel
        listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(new Color(220,220,220));
        listPanel.setOpaque(true);
        listPanel.setBounds(10, 10, 200, 580);
        listPanel.setVisible(true);


        File[] files =new File(RESULTS_PATH).listFiles();
        HashMap<String,File> fileMap = new HashMap<>();
        for (var file:files) {
            fileMap.put(file.toString().substring(RESULTS_PATH.length()),file);
        }
        list = new JList(fileMap.keySet().toArray());
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setBackground(new Color(220,220,220));
        list.setVisibleRowCount(-1);
        list.setVisible(true);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(RIGHT_ALIGNMENT);
        listPanel.add(list);

        this.getContentPane().add(listPanel);

        //TextPanel
        textPanel = new JPanel(new BorderLayout());
        textPanel.setBounds(220,10,1000,580);
        textPanel.setOpaque(true);
        textPanel.setVisible(true);
        textPanel.setBackground(Color.white);
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setAlignmentX(RIGHT_ALIGNMENT);
        scrollPane.setAlignmentY(BOTTOM_ALIGNMENT);
        textArea.setEditable(false);
        textPanel.add(scrollPane);

        this.getContentPane().add(textPanel);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() % 2 == 0) {
                    StringBuilder resultStringBuilder = new StringBuilder();
                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileMap.get(list.getSelectedValue().toString())))){
                        String line;
                        while ((line = bufferedReader.readLine())!=null){
                            resultStringBuilder.append(line).append("\n");
                        }
                    } catch (IOException exception){
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE,exception.fillInStackTrace().toString());
                    }
                    textArea.setText(resultStringBuilder.toString());
                }
            }
        });

        repaint();
    }
}
