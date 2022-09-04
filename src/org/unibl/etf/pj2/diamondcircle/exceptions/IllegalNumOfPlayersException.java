package org.unibl.etf.pj2.diamondcircle.exceptions;

public class IllegalNumOfPlayersException extends Exception {

    public IllegalNumOfPlayersException(){
        this("Nekorektna dimenzija matrice.");
    }
    public IllegalNumOfPlayersException(String msg){
        super(msg);
    }
}
