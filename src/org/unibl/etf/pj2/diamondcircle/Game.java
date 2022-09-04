package org.unibl.etf.pj2.diamondcircle;

import org.unibl.etf.pj2.diamondcircle.exceptions.IllegalMatrixDimensionException;
import org.unibl.etf.pj2.diamondcircle.exceptions.IllegalNumOfPlayersException;
import org.unibl.etf.pj2.diamondcircle.exceptions.IllegalNumberOfArgumentsException;

public class Game {


    private static final int MIN_DIM = 7;
    private static final int MAX_DIM = 10;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    private int numOfPlayers;
    private int matrixDimension;

    Game(int numOfPlayers,int matrixDimension) throws IllegalNumOfPlayersException,IllegalMatrixDimensionException {
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



}
