package org.unibl.etf.pj2.diamondcircle.gui;

import org.unibl.etf.pj2.diamondcircle.Game;
import org.unibl.etf.pj2.diamondcircle.Main;
import org.unibl.etf.pj2.diamondcircle.models.Player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import static org.unibl.etf.pj2.diamondcircle.Main.game;

public class DiamonCircleFrame extends JFrame implements ActionListener {
    JPanel topPanel;
    JPanel topLeftPanel;
    JPanel topRightPanel;
    JPanel topCenterPanel;
    JPanel topBottomPanel;

    JLabel numOfPlayedGames;
    JLabel title;
    JLabel[] playerLabel;
    JLabel description;
    JLabel currentCardLabel;
    JLabel timerLabel;

    JButton startStopBtn;
    JButton showListBtn;

    JButton[] figureBtns;

    JLabel[][] matrixLabel;

    JLabel picLabel;

    JPanel rightPanel;
    JPanel leftPanel;

    JPanel bottomPanel;
    JPanel bottomLeftPanel;
    JPanel bottomRightPanel;

    JPanel centralPanel;

    private int startStopBtnClicked = 0;
    private static final String IMG_PATH_PREFIX = "src/resources/img/";

    public DiamonCircleFrame() {

        // Podesavanje Frame-a
        this.setForeground(Color.white);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(400, 200, 1200, 850);
        this.setResizable(false);
        this.setVisible(true);
        this.setLayout(null);
        this.setTitle("DiamondCircle");

        // Podesavanje gornjeg Panel-a

        topPanel = new JPanel();
        topPanel.setBackground(Color.white);
        topPanel.setBounds(20, 20, 1140, 140);
        topPanel.setLayout(null);
        topPanel.setVisible(true);

        topLeftPanel = new JPanel();
        topLeftPanel.setBackground(Color.LIGHT_GRAY);
        topLeftPanel.setBounds(10, 10, 366, 80);
        topLeftPanel.setLayout(new BorderLayout());
        topPanel.add(topLeftPanel);
        topLeftPanel.setVisible(true);

        try {
            numOfPlayedGames = new JLabel("<html><div style='text-align: center;'>Trenutni broj odigranih<br>igara: " + (new File(Game.RESULTS_PATH).list().length) + "</div></html>");
        } catch (NullPointerException e){
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
        }
        numOfPlayedGames.setHorizontalAlignment(JLabel.CENTER);
        numOfPlayedGames.setForeground(Color.blue);
        topLeftPanel.add(numOfPlayedGames);
        numOfPlayedGames.setVisible(true);

        topCenterPanel = new JPanel();
        topCenterPanel.setBackground(Color.LIGHT_GRAY);
        topCenterPanel.setBounds(366 + 2 * 10, 10, 366, 80);
        topCenterPanel.setLayout(new BorderLayout());
        topPanel.add(topCenterPanel);
        topCenterPanel.setVisible(true);

        title = new JLabel("DiamondCircle");
        title.setHorizontalAlignment(JLabel.CENTER);
        topCenterPanel.add(title);
        title.setForeground(Color.red);
        title.setFont(new Font("MV Boli", Font.BOLD, 30));
        title.setVisible(true);

        topRightPanel = new JPanel();
        topRightPanel.setBackground(Color.LIGHT_GRAY);
        topRightPanel.setBounds(2 * 366 + 3 * 10, 10, 366, 80);
        topRightPanel.setLayout(null);
        topPanel.add(topRightPanel);
        topRightPanel.setVisible(true);

        startStopBtn = new JButton("Pokreni");
        startStopBtn.setForeground(Color.blue);
        startStopBtn.setBounds(90, 15, 200, 50);
        startStopBtn.setFocusable(false);
        startStopBtn.setVerticalAlignment(JButton.CENTER);
        startStopBtn.setHorizontalAlignment(JButton.CENTER);
        startStopBtn.addActionListener(this);
        topRightPanel.add(startStopBtn);
        startStopBtn.setVisible(true);

        topBottomPanel = new JPanel();
        topBottomPanel.setBackground(Color.LIGHT_GRAY);
        topBottomPanel.setBounds(10, 100, 1118, 30);
        topBottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 7));
        topPanel.add(topBottomPanel);
        topBottomPanel.setVisible(true);

        int num = 0;
        playerLabel = new JLabel[Main.game.getNumOfPlayers()];
        for (var player : playerLabel) {
            player = new JLabel();
            player.setText("Igrac" + (++num));
            topBottomPanel.add(player);
        }


        this.getContentPane().add(topPanel);

        // Podesavanje lijevog panela

        leftPanel = new JPanel();
        leftPanel.setBackground(Color.white);
        leftPanel.setBounds(20, 170, 200, 620);
        leftPanel.setLayout(new FlowLayout());
        leftPanel.setVisible(true);

        int i = 0;
        figureBtns = new JButton[16];
        for (var button : figureBtns) {
            button = new JButton();
            button.setText("Figura" + (++i));
            button.setPreferredSize(new Dimension(160, 33));
            button.setForeground(Color.black);
            button.setFocusable(false);
            button.setVerticalAlignment(JButton.CENTER);
            button.setHorizontalAlignment(JButton.CENTER);
            if (i > Main.game.getNumOfPlayers() * Player.NUMBER_OF_FIGURES) {
                button.setEnabled(false);
            }
            leftPanel.add(button);
        }

        this.getContentPane().add(leftPanel);


        // Podesavanje desnog panela
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.white);
        rightPanel.setBounds(960, 170, 200, 500);
        rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
        rightPanel.setVisible(true);
        this.getContentPane().add(rightPanel);

        currentCardLabel = new JLabel("Trenutna karta:");
        currentCardLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentCardLabel.setForeground(new Color(0x123456));

        ImageIcon cardIcon = new ImageIcon(new ImageIcon(IMG_PATH_PREFIX+"card1.png").getImage().getScaledInstance(180, 250, Image.SCALE_DEFAULT));
        picLabel = new JLabel(cardIcon);

        timerLabel = new JLabel("<html><div style='text-align: center;'>Vrijeme trajanja igre:<br>0 h 0 m 0 s</div></html>");

        rightPanel.add(currentCardLabel);
        rightPanel.add(picLabel);
        rightPanel.add(timerLabel);


        // Podesavanje centralnog panela

        int matrixDimension = Main.game.getMatrixDimension();
        centralPanel = new JPanel(new GridLayout(matrixDimension, matrixDimension));
        centralPanel.setBackground(Color.white);
        centralPanel.setBounds(230, 170, 720, 500);
        centralPanel.setVisible(true);

        matrixLabel = new JLabel[matrixDimension][matrixDimension];
        int k = 0;
        for (i = 0; i < matrixDimension; i++)
            for (int j = 0; j < matrixDimension; j++) {
                matrixLabel[i][j] = new JLabel(String.valueOf(++k));
                matrixLabel[i][j].setHorizontalAlignment(JLabel.CENTER);
                matrixLabel[i][j].setVerticalAlignment(JLabel.CENTER);
                matrixLabel[i][j].setVisible(true);
                centralPanel.add(matrixLabel[i][j]);
            }
        this.getContentPane().add(centralPanel);


        // Podesavanje donjeg panela
        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.white);
        bottomPanel.setBounds(230, 680, 930, 110);
        bottomPanel.setLayout(null);
        bottomPanel.setVisible(true);

        bottomLeftPanel = new JPanel();
        bottomLeftPanel.setBackground(Color.LIGHT_GRAY);
        bottomLeftPanel.setBounds(10, 10, 710, 90);
        bottomLeftPanel.setLayout(new BorderLayout());
        bottomPanel.add(bottomLeftPanel);
        bottomLeftPanel.setVisible(true);

        description = new JLabel("<html><div style='text-align: center;'>Opis znacenja karte:<br>Na potezu je igrac 2, Figura3, prelazi 3 polja, pomjera se sa pozicije 4 na 28.</div></html>");
        description.setVerticalAlignment(JLabel.CENTER);
        description.setHorizontalAlignment(JLabel.CENTER);

        bottomLeftPanel.add(description);

        bottomRightPanel = new JPanel();
        bottomRightPanel.setBackground(Color.LIGHT_GRAY);
        bottomRightPanel.setBounds(730, 10, 190, 90);
        bottomRightPanel.setLayout(null);
        bottomPanel.add(bottomRightPanel);
        bottomRightPanel.setVisible(true);

        showListBtn = new JButton("Prikaz lise fajlova");
        showListBtn.setForeground(Color.blue);
        showListBtn.setBounds(10, 10, 170, 70);
        showListBtn.setFocusable(false);
        showListBtn.setVerticalAlignment(JButton.CENTER);
        showListBtn.setHorizontalAlignment(JButton.CENTER);
        bottomRightPanel.add(showListBtn);
        showListBtn.setVisible(true);

        this.getContentPane().add(bottomPanel);

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == startStopBtn){
            if(startStopBtnClicked %2 == 0){
                startGame();
            }else{
                pauseGame();
            }
            startStopBtnClicked++;
        }
    }

    private void startGame(){
        if(startStopBtnClicked == 0){
            Thread gameDuration = timer();
            gameDuration.start();
            Thread movementMessage = movementMessage();
            movementMessage.start();
        }
        startStopBtn.setText("Zaustavi");
        game.setPause(false);
    }

    private void pauseGame(){
        startStopBtn.setText("Pokreni");
        game.setPause(true);
    }

    private Thread timer() {
        return new Thread(() -> {
            int hours = 0, minutes = 0, seconds = 0;
            while (!game.isGameOver()) {
                if (!game.isPause()) {
                    String time = String.format("%d h %d m %d s", hours, minutes, seconds);
                    this.timerLabel.setText("<html><div style='text-align: center;'>Vrijeme trajanja igre:<br>" + time+"</div></html>");
                    try {
                        Thread.sleep(game.SLEEP_TIME);
                    } catch (InterruptedException e) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
                    }
                    seconds++;
                    if (seconds >= 60) {
                        minutes++;
                        seconds %= 60;
                    }
                    if (minutes >= 60) {
                        hours++;
                        minutes %= 60;
                    }
                }
            }
            System.out.printf("Game OVER Total time: %d h %d m %d s%n", hours, minutes, seconds);
            game.setPassedTime(String.format("%d h %d m %d s", hours, minutes, seconds));
        });
    }

    private Thread movementMessage(){
        return  new Thread(()->{
           while(!game.isGameOver()){
               if(!game.isPause()){
                   description.setText(game.getMovementMsg());
                   try {
                       Thread.sleep(game.SLEEP_TIME);
                   } catch (InterruptedException e) {
                       Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
                   }
               }
           }
        });
    }
}

