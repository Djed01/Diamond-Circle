package org.unibl.etf.pj2.diamondcircle;

import org.unibl.etf.pj2.diamondcircle.exceptions.IllegalNumberOfArgumentsException;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final int NUM_OF_ARGS = 2;
    public static Game game;

    public static void main(String[] args) throws IllegalNumberOfArgumentsException {
        if (Arrays.stream(args).count() != NUM_OF_ARGS) {
            throw new IllegalNumberOfArgumentsException();
        } else {
            try {
                game = new Game(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
            } catch (Exception e) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
            }
        }
    }


}