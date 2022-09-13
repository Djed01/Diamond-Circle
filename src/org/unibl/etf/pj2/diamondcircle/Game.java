package org.unibl.etf.pj2.diamondcircle;

import org.unibl.etf.pj2.diamondcircle.exceptions.IllegalMatrixDimensionException;
import org.unibl.etf.pj2.diamondcircle.exceptions.IllegalNumOfPlayersException;
import org.unibl.etf.pj2.diamondcircle.models.Player;
import org.unibl.etf.pj2.diamondcircle.models.cards.Card;
import org.unibl.etf.pj2.diamondcircle.models.cards.NormalCard;
import org.unibl.etf.pj2.diamondcircle.models.cards.SpecialCard;
import org.unibl.etf.pj2.diamondcircle.models.figures.Figure;
import org.unibl.etf.pj2.diamondcircle.models.figures.FigureMovement;
import org.unibl.etf.pj2.diamondcircle.models.figures.GhostFigure;
import org.unibl.etf.pj2.diamondcircle.models.segments.Color;
import org.unibl.etf.pj2.diamondcircle.models.segments.Diamond;
import org.unibl.etf.pj2.diamondcircle.models.segments.Hole;
import org.unibl.etf.pj2.diamondcircle.models.segments.Segment;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {
    private static final int MIN_DIM = 7;
    private static final int MAX_DIM = 10;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;
    private static final String MOVEMENT_MASSAGE_FORMAT = "Na potezu je %s, %s prelazi %d polja, pomjera se sa pozicije %d na poziciju %d.";
    private static final String LOGGER_PATH = "src/resources/logs/Game.log";
    public static final String RESULTS_PATH = "src/resources/results/";
    private static final String CONFIG_PATH = "src/resources/config.properties";
    private static final int NUMBER_OF_NORMAL_CARDS = 10;
    private static final int NUMBER_OF_SPECIAL_CARDS = 12;
    public final ReentrantLock PAUSE_LOCK = new ReentrantLock();
    public final long SLEEP_TIME = 1000;

    //Podesavanje loggera
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
    private Card currentCard;

    private volatile boolean pause = false;
    private volatile boolean gameOver = false;

    public Segment[][] matrix;
    private final ArrayList<Integer> path = new ArrayList<>();
    private final LinkedList<Player> players = new LinkedList<>();
    private final LinkedList<Card> cards = new LinkedList<>();

    private BiConsumer<Diamond, Integer> addDiamond;
    private Consumer<Integer> removeDiamond;
    private BiConsumer<Figure, Integer> addFigure;
    private Consumer<Integer> removeFigure;
    private BiConsumer<Hole, Integer> addHole;
    private Consumer<Integer> removeHole;
    private Consumer<Card> showCard;
    private Runnable gameOverRunnable;

    public Game() {
        super();
    }

    public Game(int numOfPlayers, int matrixDimension) throws IllegalNumOfPlayersException, IllegalMatrixDimensionException {
        super();
        checkAndAddArguments(numOfPlayers, matrixDimension);
        setMatrixPath();
        configurePlayers();
        setCards();
        emptyMovementsFolder();
    }

    private void checkAndAddArguments(int numOfPlayers, int matrixDim) throws IllegalMatrixDimensionException, IllegalNumOfPlayersException {
        //Provjera odgovarajucih argumenata programa i dodjeljivanje vrijednosti istim
        if (numOfPlayers > MAX_PLAYERS || numOfPlayers < MIN_PLAYERS) {
            throw new IllegalNumOfPlayersException();
        } else {
            this.numOfPlayers = numOfPlayers;
        }
        if (matrixDim > MAX_DIM || matrixDim < MIN_DIM) {
            throw new IllegalMatrixDimensionException();
        } else {
            this.matrixDimension = matrixDim;
        }
        matrix = new Segment[matrixDim][matrixDim];
    }

    public static void deleteFiles(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFiles(f);
                } else {
                    f.delete();
                }
            }
        }
    }

    private void emptyMovementsFolder() {
        //Brisanje proslih fajlova sa putanjama figura nakon pokretanja igre
        deleteFiles(new File(FigureMovement.MOVEMENTS_PATH));
    }

    public void gameStart() {
        //Kreiranje i pokretanje niti Duh figure
        GhostFigure ghostFigure = GhostFigure.getGhostFigure();
        ghostFigure.start();

        LinkedList<Player> tempPlayers = new LinkedList<>(players);
        int playersFinished = 0;
        while (!gameOver) {
            currentCard = cards.removeFirst(); //Skidanje karte sa pocetka spila
            showCard.accept(currentCard); //Azuriranje GUI-a
            int numOfFields;
            if (currentCard instanceof NormalCard) {
                numOfFields = ((NormalCard) currentCard).getNumOfFields();
                currentPlayer = tempPlayers.removeFirst();
                currentPlayer.setNumOfFields(numOfFields);

                if (!currentPlayer.isStarted()) {
                    currentPlayer.start(); //Pokretanje niti igraca
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
                    }
                }

                synchronized (currentPlayer.LOCK) {
                    currentPlayer.LOCK.notify(); //Obavjestavanje niti igraca da pocinje sa igrom
                    try {
                        currentPlayer.LOCK.wait(); //Cekanje na igraca da zavrsi potez
                        if (!currentPlayer.isFinished()) {
                            tempPlayers.addLast(currentPlayer); //Postavljamo ga na kraj "reda"
                        } else {
                            playersFinished++;
                            if (playersFinished == numOfPlayers) {
                                gameOver = true; //Igra je zavrsena kada su svi igraci zavrsili
                            }
                        }
                    } catch (InterruptedException e) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
                    }
                }
            } else if (currentCard instanceof SpecialCard) {
                ((SpecialCard) currentCard).makeHoles(); //Pravljenje rupa
            }
            cards.addLast(currentCard); //Stavljanje karte na kraj spila
        }
        saveResults();
        gameOverRunnable.run(); //Azuriranje GUI-a nakon zavrsetka igre
    }

    private void setCards() {
        //Generisanje spila
        try {
            Properties properties = loadProperties();
            Random rand = new Random();
            int n = Integer.parseInt(properties.getProperty("n")); //Broj rupa
            for (int i = 0; i < NUMBER_OF_NORMAL_CARDS; i++) {
                cards.add(new NormalCard(1));
                cards.add(new NormalCard(2));
                cards.add(new NormalCard(3));
                cards.add(new NormalCard(4));
            }
            for (int i = 0; i < NUMBER_OF_SPECIAL_CARDS; i++) {
                cards.add(new SpecialCard(rand.nextInt(n) + 1));
            }
            Collections.shuffle(cards); //Mjesanje karti
        } catch (NumberFormatException e) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
        }
    }

    private void configurePlayers() {
        //Generisanje igraca
        List<Color> colors = Arrays.asList(Color.values());
        Collections.shuffle(colors); //Mjesanje boja u nasumican redoslijed
        colors.stream().limit(numOfPlayers).forEach(color -> players.add(new Player(color))); //Za svaku boju generisemo igraca
        Collections.shuffle(players); //Mjesanje igraca u nasumican redoslijed
    }

    public BiConsumer<Diamond, Integer> getAddDiamond() {
        return addDiamond;
    }

    public void setAddDiamond(BiConsumer<Diamond, Integer> addDiamond) {
        this.addDiamond = addDiamond;
    }

    public Consumer<Integer> getRemoveDiamond() {
        return removeDiamond;
    }

    public void setRemoveDiamond(Consumer<Integer> removeDiamond) {
        this.removeDiamond = removeDiamond;
    }

    public void setRemoveHole(Consumer<Integer> removeHole) {
        this.removeHole = removeHole;
    }

    public Consumer<Integer> getRemoveHole() {
        return removeHole;
    }

    public BiConsumer<Figure, Integer> getAddFigure() {
        return addFigure;
    }

    public void setAddFigure(BiConsumer<Figure, Integer> addFigure) {
        this.addFigure = addFigure;
    }

    public Consumer<Integer> getRemoveFigure() {
        return removeFigure;
    }

    public void setRemoveFigure(Consumer<Integer> removeFigure) {
        this.removeFigure = removeFigure;
    }

    public void setShowCard(Consumer<Card> showCard) {
        this.showCard = showCard;
    }

    public int getMatrixDimension() {
        return matrixDimension;
    }

    public String getPassedTime() {
        return passedTime;
    }

    public void setPassedTime(String passedTime) {
        this.passedTime = passedTime;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean state) {
        synchronized (PAUSE_LOCK) {
            if (!state)
                PAUSE_LOCK.notifyAll(); //Obavjestavanje svih niti
        }
        this.pause = state;
    }

    public boolean isLastField(int index) {
        return index >= path.size();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOverRunnable(Runnable gameOverRunnable) {
        this.gameOverRunnable = gameOverRunnable;
    }

    public int getPathSize() {
        return path.size();
    }

    public int getPathSegment(int index) {
        return path.get(index);
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }


    public LinkedList<Player> getPlayers() {
        return players;
    }

    public void setAddHole(BiConsumer<Hole, Integer> addHole) {
        this.addHole = addHole;
    }

    public BiConsumer<Hole, Integer> getAddHole() {
        return addHole;
    }

    public void saveResults() {
        //Cuvanje rezultata
        String fileName = RESULTS_PATH + String.format("IGRA_%d.txt", System.currentTimeMillis());
        try (PrintWriter pw = new PrintWriter(fileName)) {
            for (Player p : players) {
                pw.println(p.getResult());
            }
            pw.println("Ukupno vrijeme trajanja igre: " + getPassedTime());
        } catch (FileNotFoundException e) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
        }
    }

    public void setMatrixPath() {
        //Citanje putanje figura u zavisnosti od dimenzije matrice
        Properties properties = loadProperties();
        String pathStr = null;
        switch (matrixDimension) {
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
        assert pathStr != null;
        String[] numStr = pathStr.split(","); //Izdvajanje iz iz stringa
        Arrays.stream(numStr).forEach(s -> path.add(Integer.parseInt(s))); //Dodjeljivanje vrijednosti nizu putanje
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        FileInputStream fip;
        try {
            fip = new FileInputStream(CONFIG_PATH);
            properties.load(fip);
        } catch (IOException e) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
        }
        return properties;
    }

    public String getMovementMsg() {
        if (currentCard instanceof SpecialCard) return currentCard.toString();
        if (currentPlayer == null) return "";
        String playerName = currentPlayer.getPlayerName();
        Figure figure = currentPlayer.getCurrentFigure();
        if (figure == null) return "";
        String figureName = figure.getFigureName();
        int numOfFields = figure.getNumOfFields();
        int startPosition = figure.getStartPosition();
        int endPosition = figure.getEndPosition();
        if (startPosition == 0 || endPosition == 0) return "";
        return String.format(MOVEMENT_MASSAGE_FORMAT, playerName, figureName, numOfFields, startPosition, endPosition);
    }
}
