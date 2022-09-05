package org.unibl.etf.pj2.diamondcircle;

import org.unibl.etf.pj2.diamondcircle.exceptions.IllegalMatrixDimensionException;
import org.unibl.etf.pj2.diamondcircle.exceptions.IllegalNumOfPlayersException;
import org.unibl.etf.pj2.diamondcircle.exceptions.IllegalNumberOfArgumentsException;
import org.unibl.etf.pj2.diamondcircle.models.Player;
import org.unibl.etf.pj2.diamondcircle.models.segments.Segment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {


    private static final int MIN_DIM = 7;
    private static final int MAX_DIM = 10;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;
    private static final String LOGGER_PATH = "src/resources/logs/Game.log";
    public static final String RESULTS_PATH = "src/resources/results/";
    private static final String CONFIG_PATH = "src/resources/config.properties";

    private static final int NUMBER_OF_NORMAL_CARDS = 10;
    private static final int NUMBER_OF_SPECIAL_CARDS = 12;
    public final ReentrantLock PAUSE_LOCK = new ReentrantLock();
    public final long SLEEP_TIME = 1000;

    static {
        try {
            Handler fileHandler = new FileHandler(LOGGER_PATH, true);
            Logger.getLogger(Game.class.getName()).setUseParentHandlers(false);
            Logger.getLogger(Game.class.getName()).addHandler(fileHandler);
        } catch (IOException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
        }
    }
    private int numOfPlayers;
    private int matrixDimension;
    private String passedTime;
    private Player currentPlayer;
   // private  Card currentCard;

    private volatile boolean pause = false;
    private volatile  boolean gameOver = false;

    public Segment[][] matrix;
    private final ArrayList<Integer> path = new ArrayList<>();
    private final LinkedList<Player> players = new LinkedList<>();
    //private final LinkedList<Card> cards = new LinkedList<>();
    private Runnable gameOverRunnable;

    public Game(){
        super();
    }
    public Game(int numOfPlayers,int matrixDimension) throws IllegalNumOfPlayersException,IllegalMatrixDimensionException {
        super();
        checkAndAddArguments(numOfPlayers,matrixDimension);
    }
    private void checkAndAddArguments(int numOfPlayers, int matrixDim) throws IllegalMatrixDimensionException, IllegalNumOfPlayersException {
        if(numOfPlayers > MAX_PLAYERS || numOfPlayers < MIN_PLAYERS){
            throw new IllegalNumOfPlayersException();
        }else {
            this.numOfPlayers = numOfPlayers;
        }
        if(matrixDim > MAX_DIM || matrixDim<MIN_DIM){
            throw  new IllegalMatrixDimensionException();
        }else{
            this.matrixDimension = matrixDim;
        }
    }

    public int getMatrixDimension(){
        return matrixDimension;
    }

    public String getPassedTime() {
        return passedTime;
    }

    public void setPassedTime(String passedTime) {
        this.passedTime = passedTime;
    }

    public boolean isPause(){
        return pause;
    }

    public void setPause(boolean state){
        synchronized (PAUSE_LOCK){
            if(!pause)
                PAUSE_LOCK.notifyAll();
        }
        pause = state;
    }

    public boolean isLastField(int index){
        return  index >= path.size();
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public void setGameOverRunnable(Runnable gameOverRunnable) {
        this.gameOverRunnable = gameOverRunnable;
    }

    public int getPathSize(){
        return  path.size();
    }

    public  int getPathSegment(int index){
        return path.get(index);
    }



    public void saveResults(){
        String fileName = RESULTS_PATH + String.format("IGRA_%d.txt", System.currentTimeMillis());
        try (PrintWriter pw = new PrintWriter(fileName)){
            for(Player p : players){
                pw.println(p.getResult());
            }
            pw.println("Ukupno vrijeme trajanja igre: " + getPassedTime());
        } catch (FileNotFoundException e){
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
        }
    }

    public  void setMatrixPath(){
        Properties properties = loadProperties();
        String pathStr = null;
        switch (matrixDimension){
            case 7:
                pathStr = properties.getProperty("diamondPath7");
                break;
            case 8:
                pathStr = properties.getProperty("diamondPath8");
                break;
            case 9:
                pathStr = properties.getProperty("diamondPath9");
                break;
            case 10:
                pathStr = properties.getProperty("diamondPath10");
                break;
        }
        assert pathStr!=null;
        String[] numStr = pathStr.split(",");
        Arrays.stream(numStr).forEach(s -> path.add(Integer.parseInt(s)));
    }

    private Properties loadProperties(){
        Properties properties = new Properties();
        FileInputStream fip;
        try {
            fip = new FileInputStream(CONFIG_PATH);
            properties.load(fip);
        } catch (IOException e){
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE,e.fillInStackTrace().toString());
        }
        return properties;
    }
}
